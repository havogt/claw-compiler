PROGRAM test_column_conditional
 USE mo_column , ONLY: compute_column
 INTEGER :: i
 REAL :: q ( 1 : 5 , 1 : 10 )
 REAL :: t ( 1 : 5 , 1 : 10 )
 REAL :: z ( 1 : 5 , 1 : 10 )
 INTEGER :: nproma
 INTEGER :: nz
 INTEGER :: p

 nproma = 5
 nz = 10
 DO p = 1 , nproma , 1
  q ( p , 1 : nz ) = 6.0
  z ( p , 1 : nz ) = 5.0
  t ( p , 1 : 6 ) = 2.0
  t ( p , 6 : nz ) = 0.0
 END DO
 CALL compute_column ( nz , q ( : , : ) , t ( : , : ) , z ( : , : ) , nproma =&
  nproma )
 PRINT * , ( q ( 1 , i ) , i = 1 , 10 , 1 )
END PROGRAM test_column_conditional

