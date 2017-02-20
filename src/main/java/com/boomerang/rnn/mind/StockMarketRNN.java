package com.boomerang.rnn.mind;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.datavec.api.split.ListStringSplit;
import org.datavec.api.records.reader.BaseRecordReader;
import org.datavec.api.records.reader.impl.collection.ListStringRecordReader;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.GravesLSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.boomerang.rnn.util.Interpreter;

@Component
public class StockMarketRNN {
    private static final Logger LOG = Logger.getLogger(StockMarketRNN.class.getName());

    @Value( "${boomerang.mind.inputcolumns}" )
    int inputcolumns;

    @Value( "${boomerang.mind.outputs}" )
    int outputs;
    
    @Value( "${boomerang.mind.batch}" )
    int batch;

    @Value( "${boomerang.mind.learningrate}" )
    double learningrate;

    @Value( "${boomerang.mind.seed}" )
    int seed;

    @Value( "${boomerang.mind.rmsdecay}" )
    double rmsdecay;

    @Value( "${boomerang.mind.l2}" )
    double l2;

    @Value( "${boomerang.mind.lstmnodes}" )
    int lstmnodes;

    @Value( "${boomerang.mind.tbptt}" )
    int tbptt;

    @Value( "${boomerang.mind.timeseries}" )
    int timeseries;

    @Value( "${boomerang.mind.score}" )
    int score;

    @Value( "${boomerang.mind.epochs}" )
    int epochs;
    
    @Value( "${boomerang.mind.projection}" )
    int projection;
    
    @Value( "${boomerang.mind.activation}" )
    String activation;
    
    @Value( "${boomerang.mind.weightinit}" )
    String weightinit;
    
    @Value( "${boomerang.mind.loss}" )
    String loss;

    public StockMarketRNN() {
        LOG.info("stockMarketRNN instantiated");
    }

    public INDArray learn(List<String> data) {
        double quote = Interpreter.recentQuote(data);
        if (quote == 0d) {
            LOG.severe("QOC not retreived, forecast will be affected");
        } else {
            LOG.log(Level.INFO, "Quote on previous market close: {0}", quote);
        }
        
        List<List<String>> dataDltd = Interpreter.delimit(data);
        ListStringSplit split = new ListStringSplit(dataDltd);
        BaseRecordReader rec = new ListStringRecordReader();

        try {
            rec.initialize(split);
        } catch (IOException | InterruptedException e) {
            LOG.log(Level.SEVERE, "Data iteration failed- {0}", e.getMessage());
            shutdown(rec);
            return null;
        }

        DataSetIterator dataSetIterator = new RecordReaderDataSetIterator(rec, batch);
        DataNormalization norm = new NormalizerMinMaxScaler();
        // norm.fit(dataSetIterator);
        
        MultiLayerNetwork rnn = new MultiLayerNetwork(config()); 
        rnn.init(); 
        rnn.setListeners(new ScoreIterationListener(score)); 

        for (int i = 0; i < epochs; i++) {
            while (dataSetIterator.hasNext()) {
                DataSet dataSet = dataSetIterator.next();
                DataSet r3Data = expandDims(dataSet);
                norm.fit(r3Data);
                norm.preProcess(r3Data);
                rnn.fit(r3Data);
            }
            dataSetIterator.reset();
        }
        
        // Deep belief 
        DataSet recentSamples = recentWindow(dataSetIterator);
        norm.fit(recentSamples);
        INDArray results = Nd4j.zeros(1, 1, projection);
        INDArray initializer = Nd4j.zeros(1,1,1);
        initializer.putScalar(new int[] {0,0,0}, quote);
        INDArrayIndex pointZero = NDArrayIndex.point(0);
        /* TEST */
        results.putScalar(new int[] {0,0,0}, quote);
        norm.transform(results);
        /* END */
        norm.transform(initializer);
        StringBuilder buil = new StringBuilder().append('\n');
        for (int i = 0; i < projection; i++) {
        	buil.append(i+": "+initializer.getDouble(0,0,0)+'\n');
        	INDArray step = rnn.rnnTimeStep(results);
        	INDArrayIndex[] nextPos = {pointZero,pointZero,NDArrayIndex.point(i)};
            results.put(nextPos, step);
            norm.transform(results);
            initializer.put(new INDArrayIndex[] {pointZero, pointZero, pointZero}, step);
        }
        LOG.info(buil.toString());
        norm.revertFeatures(results);
        shutdown(rec);
        return results;
    }
    
    public MultiLayerConfiguration config() {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder() 
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).iterations(1) 
            .learningRate(learningrate) 
            .rmsDecay(rmsdecay) 
            .seed(seed) 
            .regularization(true) 
            .l2(l2) 
            .weightInit(WeightInit.valueOf(weightinit)) 
            .updater(Updater.RMSPROP) 
            .list() 
            .layer(0, new GravesLSTM.Builder().nIn(inputcolumns).nOut(lstmnodes) 
                    .activation(Activation.valueOf(activation)).build()) 
            .layer(1, new GravesLSTM.Builder().nIn(lstmnodes).nOut(lstmnodes) 
                    .activation(Activation.valueOf(activation)).build()) 
            .layer(2, new RnnOutputLayer.Builder(LossFunction.valueOf(loss))
                    .activation(Activation.SOFTMAX)
            .nIn(lstmnodes).nOut(outputs).build()) 
                    .backpropType(BackpropType.TruncatedBPTT)
                    .tBPTTForwardLength(tbptt)
                    .tBPTTBackwardLength(tbptt) 
                    .pretrain(false).backprop(true) 
            .build();  
        return conf;
    }
       
    /**
     * Configures 2D matrices as 3D matrices.
     * <p>
     * Adds time series nature to data.
     * @param dataset- 2D DataSet
     * @return 3D Time Series DataSet
     */
    private DataSet expandDims(DataSet dataset) {
        INDArray dataFeatures = dataset.getFeatures();
        INDArray dataLabels = dataset.getLabels();
        INDArray timelineFeatures = expandDims(dataFeatures);
        INDArray timelineLabels = expandDims(dataLabels);
        return new DataSet(timelineFeatures,timelineLabels);
    }
    
    public INDArray expandDims(INDArray array) {
        int[] tuple = array.shape();
        INDArray r3 = Nd4j.create(new int[]{ 4, 1, array.rows() },'f');
        for (int i = 0; i < array.rows(); i++) {
            INDArray timeDataRow = array.get(NDArrayIndex.point(i), NDArrayIndex.interval(1, tuple[1] - 1)).dup();
            INDArray timeDataCol = timeDataRow.permute(1,0);
            int timeSeriesPoint = array.getInt(new int[]{i,0});
            INDArrayIndex[] target = {NDArrayIndex.all(),NDArrayIndex.point(0),
                NDArrayIndex.point(i)};
            r3.put(target,timeDataCol);
        }
        return r3;
    }
	
    public void shutdown(BaseRecordReader recordReader) {
        try {
            recordReader.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not close record reader- {0}", e.getMessage());
        }
    }
    
    // Get recent window, exlcuding a few on the projection's edge.
    private DataSet recentWindow(DataSetIterator dataSetIterator) {
        DataSet recentData = new DataSet();
        while (dataSetIterator.hasNext()) {
            recentData = dataSetIterator.next(projection);
            if (dataSetIterator.hasNext()) {
                if (dataSetIterator.next().numExamples() < projection) {
                    break;
                } else if (!dataSetIterator.hasNext()) break;
            }
        }
        recentData = expandDims(recentData);
        return recentData;
    }
}
