#!/bin/bash

operation_count=250000
thread_count=192
time_compression_ratio=0.1

# used to determine workload mode, if false just produces updates for kafka queue, if yes then runs on actual SUT
consume_mode=false

ignore_scheduled_start_times=true

graph_name=sf10_updates

# locator should point to remote-objects.yaml
locatordef=/hdd1/gp/janusgraph/conf/remote.yaml
locatorlar16=/hdd1/gp/janusgraph/conf/remote-objects.yaml.16
locatorlar8=/hdd1/gp/janusgraph/conf/remote-objects.yaml.8

locator=$locatorlar8

# configuration params for benchmark run
conf_onehop=/hdd1/gp/ldbc_driver/configuration/ldbc-q11-onehop.properties
conf_twohop=/hdd1/gp/ldbc_driver/configuration/ldbc-q12-twohop.properties
conf_path=$conf_twohop

# dataset location
dataset_location_sf3=/home/apacaci/ldbc-gremlin/ldbc_snb_datagen/datasets/sf3_updates
dataset_location_sf10=/home/apacaci/ldbc-gremlin/ldbc_snb_datagen/datasets/sf10_updates
dataset_location_sf1000=/home/apacaci/ldbc-gremlin/ldbc_snb_datagen/datasets/sf1000_updates_friendship/

dataset_location=$dataset_location_sf1000

parameters_dir=$dataset_location/substitution_parameters
updates_dir=$dataset_location/social_network

# Database Implementation
db=ca.uwaterloo.cs.ldbc.interactive.gremlin.GremlinDb

# jar file for the workload implementation
workload_impl_gremlin=/hdd1/gp/graph-benchmarking/snb-interactive-gremlin/target/snb-interactive-gremlin-1.0-SNAPSHOT-jar-with-dependencies.jar
workload_impl=$workload_impl_gremlin

# first argument is a boolean. Run debug mode if given true
if [ "$1" = true ] ; then
    JAVA="java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=1044"
else
    JAVA="java"
fi

exec $JAVA -Djava.util.logging.config.file=logging.properties -cp "target/jeeves-0.3-SNAPSHOT.jar:src/main/resources:$workload_impl" com.ldbc.driver.Client -w com.ldbc.driver.workloads.ldbc.snb.interactive.LdbcSnbInteractiveWorkload -oc $operation_count -P $conf_path -p "ldbc.snb.interactive.parameters_dir|$parameters_dir" -p "ldbc.snb.interactive.updates_dir|$updates_dir" -p "graphName|$graph_name" -p "locator|$locator" -db $db -tc $thread_count -tcr $time_compression_ratio -ignore_scheduled_start_times $ignore_scheduled_start_times -cu $consume_mode
