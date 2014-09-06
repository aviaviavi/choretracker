#!/bin/bash

rm -rf env
virtualenv env
. env/bin/activate
pip install bottle
