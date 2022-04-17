#!/bin/python

import xml.etree.ElementTree as et

pom_root = et.parse(open('SampleApp.Cli/pom.xml')).getroot()
# "http://maven.apache.org/POM/4.0.0" is the namespace as seen in the POM file MeterSimulator.Cli/pom.xml
version_element = pom_root.find('{http://maven.apache.org/POM/4.0.0}version').text
print(version_element)