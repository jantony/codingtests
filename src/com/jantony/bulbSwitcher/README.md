Toggle bulbs - 314
========
1. All bulbs are turned off
2. You first turn on all the bulbs.
3. Then, you turn off every second bulb.(2, 4, 6, …)
4. On the third round, you toggle every third bulb.(3, 6, 9, …)
5. For the ith round, you toggle every i bulb.(i, 2i, 3i, …)
6. For the nth round, you only toggle the last bulb.(n)

Solution
=======

The bulb is toggled `number of factors` times.
Factors always come in pairs unless it is perfect square. So only perfect squares are on.
The problem becomes number of perfect squares equal to or below n.
