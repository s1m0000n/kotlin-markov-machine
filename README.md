# Kotlin Markov Machine

Toy interpreter for Normal Markov Algorithms

## Example

### Adding rules

Binary number (0, 1) to unary (|) representation

```
>> 1 -> 0|
>> |0 -> 0||
>> 0 -> 
```

### Regular running mode

Just write expressions like rules! (not containing ->, =>)

```
>> 101
|||||
>> 1010101
|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
>> 1
|
>> 0

>> 000000

>> 
```

### Verbose mode

```
>> :v 1111
[verbose mode] Press Enter to exec the next step, :stop to leave
1111 >> 
[0. Rule: "1" -> "0|"] 0|111  
[0. Rule: "1" -> "0|"] 0|0|11  
[0. Rule: "1" -> "0|"] 0|0|0|1 
[0. Rule: "1" -> "0|"] 0|0|0|0|  
[1. Rule: "|0" -> "0||"] 00|||0|0|  
[1. Rule: "|0" -> "0||"] 00||0|||0|  
[1. Rule: "|0" -> "0||"] 00|0|||||0|  
[1. Rule: "|0" -> "0||"] 000|||||||0| 
[1. Rule: "|0" -> "0||"] 000||||||0|||  
[1. Rule: "|0" -> "0||"] 000|||||0|||||  
[1. Rule: "|0" -> "0||"] 000||||0|||||||  
[1. Rule: "|0" -> "0||"] 000|||0|||||||||  
[1. Rule: "|0" -> "0||"] 000||0|||||||||||  
[1. Rule: "|0" -> "0||"] 000|0||||||||||||| 
[1. Rule: "|0" -> "0||"] 0000||||||||||||||| 
[2. Rule: "0" -> ""] 000||||||||||||||| 
[2. Rule: "0" -> ""] 00||||||||||||||| 
[2. Rule: "0" -> ""] 0||||||||||||||| 
[2. Rule: "0" -> ""] ||||||||||||||| 
No more rules applicable => Stop 
|||||||||||||||
>> 
```