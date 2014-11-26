package edu.uchicago.mpcs53013.streamingWeather;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import scala.Tuple2;

import com.google.common.collect.Lists;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.StorageLevels;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import edu.uchicago.mpcs53013.weatherSummary.WeatherSummary;

import java.util.regex.Pattern;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

import org.apache.spark.Logging;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.log4j.*;
/**
 * Counts words in UTF8 encoded, '\n' delimited text received from the network every second.
 *
 * Usage: JavaNetworkWordCount <hostname> <port>
 * <hostname> and <port> describe the TCP server that Spark Streaming would connect to receive data.
 *
 * To run this on your local machine, you need to first run a Netcat server
 * `$ nc -lk 9999`
 * and then run the example
 * `•	bin/spark-submit --master spark://mpcs53013-VirtualBox:7077 --class edu.uchicago.mpcs53013.spark.JavaNetworkWordCount `
 */
public final class StreamingWeather {
	private static final Pattern SPACE = Pattern.compile(" ");
	static TDeserializer deserializer = new TDeserializer(new TJSONProtocol.Factory());
	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("Usage: JavaStreamingWeather <hostname> <port> <Cassandra Host>");
			System.exit(1);
		}

		boolean log4jInitialized = Logger.getLogger("spark").getAllAppenders().hasMoreElements();
		 if (!log4jInitialized) {
			// We first log something to initialize Spark's default logging, then we override the
			// logging level.
			Logger.getLogger("spark").info("Setting log level to [WARN] for streaming example." +
			" To override add a custom log4j.properties to the classpath.");
			Logger.getLogger("spark").setLevel(Level.WARN);
			Logger.getRootLogger().setLevel(Level.WARN);
			}		
		// Create the context with a 1 second batch size
		SparkConf sparkConf = new SparkConf().setAppName("StreamingWeather");
	    sparkConf.setMaster(args[2]);
		sparkConf.set("spark.cassandra.connection.host", args[3]);
		JavaStreamingContext ssc = new JavaStreamingContext(sparkConf, new Duration(1000));
		// Create a JavaReceiverInputDStream on target ip:port and count the
		// words in input stream of \n delimited text (eg. generated by 'nc')
		// Note that no duplication in storage level only for running locally.
		// Replication necessary in distributed scenario for fault tolerance.
		JavaReceiverInputDStream<String> lines = ssc.socketTextStream(
				args[0], Integer.parseInt(args[1]), StorageLevels.MEMORY_AND_DISK_SER);
		JavaDStream<CassandraWeatherSummary> cassandraWeatherSummaries 
		   = lines.map(new Function<String, CassandraWeatherSummary>() {
			@Override
			public CassandraWeatherSummary call(String x) {
				WeatherSummary weatherSummary = new WeatherSummary();
				try {
					deserializer.fromString(weatherSummary, x);
				} catch (TException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return new CassandraWeatherSummary(weatherSummary);
			}
		});        
		javaFunctions(cassandraWeatherSummaries)
        .writerBuilder("weather", "latest_weather", mapToRow(CassandraWeatherSummary.class))
        .saveToCassandra();

		ssc.start();
		ssc.awaitTermination();
	}
}