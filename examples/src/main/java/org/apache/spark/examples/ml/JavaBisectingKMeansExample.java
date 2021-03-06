/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.examples.ml;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
// $example on$
import org.apache.spark.ml.clustering.BisectingKMeans;
import org.apache.spark.ml.clustering.BisectingKMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.VectorUDT;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
// $example off$


/**
 * An example demonstrating a bisecting k-means clustering.
 */
public class JavaBisectingKMeansExample {

  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("JavaBisectingKMeansExample");
    JavaSparkContext jsc = new JavaSparkContext(conf);
    SQLContext jsql = new SQLContext(jsc);

    // $example on$
    JavaRDD<Row> data = jsc.parallelize(Arrays.asList(
      RowFactory.create(Vectors.dense(0.1, 0.1, 0.1)),
      RowFactory.create(Vectors.dense(0.3, 0.3, 0.25)),
      RowFactory.create(Vectors.dense(0.1, 0.1, -0.1)),
      RowFactory.create(Vectors.dense(20.3, 20.1, 19.9)),
      RowFactory.create(Vectors.dense(20.2, 20.1, 19.7)),
      RowFactory.create(Vectors.dense(18.9, 20.0, 19.7))
    ));

    StructType schema = new StructType(new StructField[]{
      new StructField("features", new VectorUDT(), false, Metadata.empty()),
    });

    DataFrame dataset = jsql.createDataFrame(data, schema);

    BisectingKMeans bkm = new BisectingKMeans().setK(2);
    BisectingKMeansModel model = bkm.fit(dataset);

    System.out.println("Compute Cost: " + model.computeCost(dataset));

    Vector[] clusterCenters = model.clusterCenters();
    for (int i = 0; i < clusterCenters.length; i++) {
      Vector clusterCenter = clusterCenters[i];
      System.out.println("Cluster Center " + i + ": " + clusterCenter);
    }
    // $example off$

    jsc.stop();
  }
}
