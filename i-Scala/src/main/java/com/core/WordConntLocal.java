package com.core;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

public class WordConntLocal {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("SparkJava").setMaster("local");

        //初始化Spark应用程序所需的一些核心组件
        JavaSparkContext sc = new JavaSparkContext(conf);

        //创建一个初始的RDD，输入源：HDFS文件，本地文件等等
        JavaRDD<String> lines = sc.textFile("hdfs://172.16.104.203:9000/home/spark/employee10000w/part-m-00000");

        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {

            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split(",")).iterator();
            }
        });

        //将每个元素映射成（v1,v2）的类型
        JavaPairRDD<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String word) throws Exception {
                return new Tuple2<String, Integer>(word, 1);
            }
        });

        //以单词作为KEY。统计每个单词出现的次数
        JavaPairRDD<String, Integer> wordCounts = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {

                return v1 + v2;
            }
        });

        wordCounts.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> argo) throws Exception {
                System.out.println(argo._1+" 出现了： "+argo._2+" 次.");
            }
        });
    }
}
