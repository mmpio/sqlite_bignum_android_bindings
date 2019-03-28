# sqlite_bignum_android_bindings
SQLite Android bindings with support for BigNum functions
This repo is a clone from the SQLite_android_bindings.  
The underlying sqliteX.so library is linked to GMP library
in order to compute big numbers.

Five bignum handling functions have been added :  
-function bigint(x, y) -> x * 10^y  
-function bigadd(x, y) -> x + y  
-function bigsub(x, y) -> x - y  
-function bigmul(x, y) -> x * y  
-function bigdiv(x, y, z) -> x / y rounded to z decimals  
-aggregate bigsum -> like sum but with bignum support
