!!

This file contains executable examples
Array operations. Examine the code
and run then run this program.

Arrays must dimensioned (DIMed) before 
the can be used. The DIM tells the
program how many lists or lists
of lists you will want.
!!

DIM array_1[10]
DIM array_2[10, 5], array_3[10,5,3]

! The following examples fills a
! numeric array with successive numbers
! and then prints the array.

print "Example 1"

dim able[2,3,4]
p = 0
for i=1 to 2
	for j=1 to 3
		for k=1 to 4
			able[i,j,k] = p
			p=p+1
		next k
	next j
next i

print "Array able[] loaded"

buffer$=""
for i=1 to 2
	for j=1 to 3
		for k=1 to 4
			s$ = format$("##", able[i,j,k])
			buffer$ = buffer$+ ", " + s$
		next k
		print i,j, buffer$
		buffer$=""
	next j
next i

! The following example fills a 
! string array with successive values
! and then prints the array

print "Example 2"

dim able$[2,3,4]
p = 1
for i=1 to 2
	for j=1 to 3
		for k=1 to 4
			able$[i,j,k] = format$("##",p)
			p=p+1
		next k
	next j
next i

print "Array able$[] loaded"

buffer$=""
for i=1 to 2
	for j=1 to 3
		for k=1 to 4
			buffer$ = buffer$+ ", " +able$[i,j,k]
		next k
		print i,j, buffer$
		buffer$=""
	next j
next i


! Array.xxxx Commands

!  Start of demos using numeric
! arrays

array.load x[], 3,5,6,9,1~
4,2,7,8,5,4,7,4,5,6
title$="Original load"
gosub ShowNumbers

array.sort x[]
title$="Sorted"
gosub ShowNumbers

array.reverse x[]
title$="Reversed"
gosub ShowNumbers

array.shuffle x[]
title$="Shuffled"
gosub ShowNumbers

! Skip over subroutine

goto skip

! Subroutine to show the
! numeric array values

ShowNumbers:
print title$
array.length length,x[]
for i=1 to length
print x[i];", ";
next i
print ""
return

skip:

!  Start of  numeric array information
!  extraction commands

array.sum sum, x[]
print "Sum = "; sum

array.average avg, x[]
print "Average = "; avg

array.variance var,x[]
print "Variance = "; var

array.std_dev sd,x[]
print "Standard Deviation = "; sd

array.min min, x[]
print "Min = "; min

array.max max, x[]
print "Max = "; max

! Start of string array demos

array.load z$[],"abc","def","ghi","jkl"~
"mno", "pqr", "stu", "vwx", "yz*"

print "Loaded string array"

array.length length,z$[]
for i=1 to length
print z$[i];" ";
next i
print ""

print "Reversed string array"
array.reverse z$[]

array.length length,z$[]
for i=1 to length
print z$[i];" ";
next i
print ""

end
