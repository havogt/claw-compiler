MODULE mo_column

CONTAINS
 SUBROUTINE compute_column ( nz , q , t , nproma )
  INTEGER , INTENT(IN) :: nz
  REAL , INTENT(INOUT) :: t ( : , : )
  REAL , INTENT(INOUT) :: q ( : , : )
  INTEGER :: k
  REAL :: c
  INTEGER , INTENT(IN) :: nproma
  INTEGER :: proma

  c = 5.345
  DO k = 1 , nz , 1
   DO proma = 1 , nproma , 1
    t ( nproma , k ) = c * k
   END DO
   DO proma = 1 , nproma , 1
    q ( nproma , k ) = q ( nproma , k - 1 ) + t ( nproma , k ) * c
   END DO
  END DO
  DO proma = 1 , nproma , 1
   q ( nproma , nz ) = q ( nproma , nz ) * c
  END DO
 END SUBROUTINE compute_column

END MODULE mo_column
