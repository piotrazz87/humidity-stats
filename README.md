# humidity-stats

Command line program that calculates statistics from humidity sensor data.

# Background story

The sensors are in a network, and they are divided into groups. Each sensor submits its data to its group leader. Each leader produces a daily report file for a group. The network periodically re-balances itself, so the sensors could change the group assignment over time, and their measurements can be reported by different leaders. The program should help spot sensors with highest average humidity.

# Input

Program takes one argument: a path to directory
Directory contains many CSV files (*.csv), each with a daily report from one group leader
Format of the file: 1 header line + many lines with measurements
Measurement line has sensor id and the humidity value
Humidity value is integer in range [0, 100] or NaN (failed measurement)
The measurements for the same sensor id can be in the different files

# Few notes

1. Program will work more efficiently if each stream will be processed parallel.
2. Need to add some exception handling.



