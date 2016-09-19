**LOAN-API**

**TECHNOLOGIES**

```
Scala 2.10.4
Play 2.2.3
Scalatest 2.1.5
```

**Notes**

There are 3 endpoints:

```
POST        /loan
POST        /offer
GET         /offer/:id

```

There are 2 repos:

```
OfferRepo
LoanRepo
```
I have modeled the repos thinking in nosql databases i.e. mongodb


There are a service that contains the real logic of the exercise (calculate the offer)

```OfferCalculator```

with a method:

```def calculateOffer(loanId: String): Future[Either[Error, CurrentOffer]]```

**Testing strategy**

1. End to end test
2. Controller unit tests
3. Unit test


