package com.boomerang.test;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.boomerang.rnn.dao.GoogleFinance;
import com.boomerang.rnn.mind.StockMarketRNN;
import com.boomerang.rnn.service.AppService;
import com.boomerang.rnn.util.Interpreter;
import com.boomerang.rnn.util.JsonHandler;

import java.io.IOException;
import org.datavec.api.records.reader.BaseRecordReader;
import org.datavec.api.records.reader.impl.collection.ListStringRecordReader;
import org.datavec.api.split.ListStringSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.beans.factory.annotation.Value;

@Test
@ContextConfiguration(locations = { "classpath:boomerang-test-context.xml" })
public class ApiTest extends AbstractTestNGSpringContextTests{
    private static final Logger LOG = Logger.getLogger(ApiTest.class.getName());
    int elements = 0;

    @Autowired
    JsonHandler jsonAgent;

    @Autowired
    GoogleFinance googleFinance;

    @Autowired
    StockMarketRNN stockMarketRNN;

    @Autowired
    AppService appService;
    
    @Value( "${boomerang.test.symbol}" )
    String symbol;

    @Test()
    public void valueTest() {
        LOG.info("Testing API");

        List<String> stockData = googleFinance.fetchData(symbol);
        LOG.log(Level.INFO, "Data units imported: {0}", stockData.size());
        // testDataTransform(stockData);
        INDArray steps = stockMarketRNN.learn(stockData);
        readArray(steps);
        LOG.info("SUCCESS");
    }   
    
    // Data translation designs
    public void testDataTransform(List<String> base) {
        List<List<String>> dataDltd = Interpreter.delimit(base);
        ListStringSplit split = new ListStringSplit(dataDltd);
	BaseRecordReader rec = new ListStringRecordReader();
        try {
            rec.initialize(split);
	} catch (IOException | InterruptedException e) {
            LOG.log(Level.SEVERE, "Data iteration failed- {0}", e.getMessage());
            stockMarketRNN.shutdown(rec);
            return;
	}
        DataSetIterator dataSetIterator = new RecordReaderDataSetIterator(rec, 31);
        
        while (dataSetIterator.hasNext()) {
            DataSet ds = dataSetIterator.next();
            INDArray arr = ds.getLabels();
            
            int[] tuple = arr.shape();
            // LOG.log(Level.INFO, "Shape of minibatch feature matrix: {0}x{1}", new String[]{String.valueOf(tuple[0]), String.valueOf(tuple[1])});
            INDArray r3 = Nd4j.create(new int[]{ 4, 1, 366 },'f');
            for (int i = 0; i < arr.rows(); i++) {
            try {
                INDArray timeDataRow = arr.get(NDArrayIndex.point(i), NDArrayIndex.interval(1, tuple[1] - 1)).dup();
                INDArray timeDataCol = timeDataRow.permute(1,0);
                // readRowVector(timeDataRow);
                // readColVector(timeDataCol);
                int timeSeriesPoint = arr.getInt(new int[]{i,0});
                INDArrayIndex[] target = {NDArrayIndex.all(),NDArrayIndex.point(0),NDArrayIndex.point(timeSeriesPoint)};
                r3.put(target,timeDataCol);
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.severe("crashed");
                    return;
                }
            }
            //readArray(r3);
        }
    }
    
    private void readArray(INDArray indarray) {
        LOG.info("Printing 3D array-");
        int[] shape = indarray.shape();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < shape[0]; i++) {
            for (int j = 0; j < shape[1]; j++) {
                for (int k = 0; k < shape[2]; k++) {
                    double v = indarray.getDouble(i,j,k);
                    builder.append('\n')
                        .append('(')
                        .append(i)
                        .append(',')
                        .append(j)
                        .append(',')
                        .append(k)
                        .append(')')
                        .append(':')
                        .append(v);
                }
            }
        }
        LOG.info(builder.toString());
    }
    
    private void readRowVector(INDArray array) {
        StringBuilder builder = new StringBuilder();
        builder.append('\n');
        for(int i = 0; i < array.columns(); i++) {
            builder.append(array.getDouble(0,i)).append(',');
        }
        builder.setLength(builder.length() - 1);
        LOG.info(builder.toString());
    }
    
    private void readColVector(INDArray array) {
        StringBuilder builder = new StringBuilder();
        builder.append('\n');
        for(int i = 0; i < array.rows(); i++) {
            builder.append(array.getDouble(i,0)).append('\n');
        }
        builder.setLength(builder.length() - 1);
        LOG.info(builder.toString());
    }
}
