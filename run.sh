#!/bin/bash

for i in {0..15}
do
   echo taskset -c 0-$i lein run $1 $(($i + 1))
   taskset -c 0-$i lein run $1 $(($i + 1))
done