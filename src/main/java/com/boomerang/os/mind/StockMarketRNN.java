package com.boomerang.os.mind;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.datavec.api.split.ListStringSplit;
import org.datavec.api.records.reader.BaseRecordReader;
import org.datavec.api.records.reader.impl.collection.ListStringRecordReader;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
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

import com.boomerang.os.util.Interpreter;

@Component
public class StockMarketRNN {
	private static final Logger LOG = Logger.getLogger(StockMarketRNN.class.getName());
	
	@Value( "${boomerang.mind.inputcolumns}" )
	int inputcolumns;
	
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

	@Value( "${boomerang.mind.outputs}" )
	int outputs;
	
	@Value( "${boomerang.mind.score}" )
	int score;
	
	@Value( "${boomerang.mind.epochs}" )
	int epochs;
	
	public StockMarketRNN() {
		LOG.info("stockMarketRNN instantiated");
	}
	
	public void learn(List<String> data) {
		List<List<String>> dataDltd = Interpreter.delimit(data);
		ListStringSplit split = new ListStringSplit(dataDltd);
		BaseRecordReader rec = new ListStringRecordReader();
		
		try {
			rec.initialize(split);
		} catch (IOException | InterruptedException e) {
			LOG.severe("Data iteration failed- " + e.getMessage());
			shutdown(rec);
			return;
		}
		
		DataSetIterator dataSetIterator = new RecordReaderDataSetIterator(rec, batch);
		MultiLayerNetwork rnn = new MultiLayerNetwork(config()); 
		rnn.init(); 
		rnn.setListeners(new ScoreIterationListener(score)); 
		
		for (int i = 0; i < epochs; i++) {
			while (dataSetIterator.hasNext()) {
				DataSet dataSet = dataSetIterator.next();
				rnn.fit(dataSet);
			}
			dataSetIterator.reset();
		}
		
		shutdown(rec);
	}
	
	public MultiLayerConfiguration config() {
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder() 
			.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).iterations(1) 
			.learningRate(learningrate) 
			.rmsDecay(rmsdecay) 
			.seed(seed) 
			.regularization(true) 
			.l2(l2) 
			.weightInit(WeightInit.XAVIER) 
			.updater(Updater.RMSPROP) 
			.list() 
			.layer(0, new GravesLSTM.Builder().nIn(inputcolumns).nOut(lstmnodes) 
				.activation(Activation.TANH).build()) 
			.layer(1, new GravesLSTM.Builder().nIn(lstmnodes).nOut(lstmnodes) 
				.activation(Activation.TANH).build()) 
			.layer(2, new RnnOutputLayer.Builder(LossFunction.MCXENT).activation(Activation.SOFTMAX)
			.nIn(lstmnodes).nOut(outputs).build()) 
				.backpropType(BackpropType.TruncatedBPTT).tBPTTForwardLength(tbptt).tBPTTBackwardLength(tbptt) 
				.pretrain(false).backprop(true) 
			.build();  
		return conf;
	}
	
	private void shutdown(BaseRecordReader recordReader) {
		try {
			recordReader.close();
		} catch (IOException e) {
			LOG.severe("Could not close record reader- " + e.getMessage());
		}
	}
}
