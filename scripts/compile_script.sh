#!/usr/bin/env bash

for i in *.ll
do
    name=${i::-3}
    clang-4.0 "$i" -o "$name.out"
done
