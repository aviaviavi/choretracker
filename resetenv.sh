#!/bin/bash

rm -rf env
virtualenv env
source env/bin/activate
pip install bottle
