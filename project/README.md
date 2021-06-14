# Don't Wreck My House

### Overview
The application user is an accommodation administrator. They pair guests to hosts to make reservations.

### Requirements 
* The administrator may view existing reservations for a host.
  * The user may enter a value that uniquely identifies a host or they can search for a host and pick one out of a list.
  * If the host is not found, display a message.
  * If the host has no reservations, display a message.
  * Show all reservations for that host.
  * Show useful information for each reservation: the guest, dates, totals, etc.
  * Sort reservations in a meaningful way.
  
* The administrator may create a reservation for a guest with a host.
  * The user may enter a value that uniquely identifies a guest or they can search for a guest and pick one out of a list.
  * The user may enter a value that uniquely identifies a host or they can search for a host and pick one out of a list.
  * Show all future reservations for that host so the administrator can choose available dates.
  * Enter a start and end date for the reservation.
  * Calculate the total, display a summary, and ask the user to confirm. The reservation total is based on the host's standard rate and weekend rate. For each day in the reservation, determine if it is a weekday or a weekend. If it's a weekday, the standard rate applies. If it's a weekend, the weekend rate applies.
  * On confirmation, save the reservation.
  * Validation
    * Guest, host, and start and end dates are required.
    * The guest and host must already exist in the "database". Guests and hosts cannot be created.
    * The start date must come before the end date.
    * The reservation may never overlap existing reservation dates.
    * The start date must be in the future.
* The administrator may edit existing reservations.
  * Find a reservation.
  * Start and end date can be edited. No other data can be edited.
  * Recalculate the total, display a summary, and ask the user to confirm.
  * Validation
    * Guest, host, and start and end dates are required.
    * The guest and host must already exist in the "database". Guests and hosts cannot be created.
    * The start date must come before the end date.
    * The reservation may never overlap existing reservation dates.
* The administrator may cancel a future reservation.
  * Find a reservation.
  * Only future reservations are shown.
  * On success, display a message.
  * Validation
    * You cannot cancel a reservation that's in the past.

### Planning

#### Project Set Up 
* [X] Create the Maven Project 
* [ ] Add JUnit Dependency
* [ ] Add Spring DI Dependency
* [ ] Stub out package structure
* [ ] Stub out the App main method
* All above ^: Day 1 Evening 1 hour

#### Models
* Package: learn.dontwreckmyhouse.models

* [ ] Define ` Host ` model (Day 1 Evening - 20min)
  * `id` - UUID - `String`
  * `last_name` - `String`
  * `email` - `String`
  * `phone` - `String`
  * `address` - `String`
  * `city` - `String`
  * `state` - `String`
  * `postal_code` - `String`
  * `standard_rate` - `BigDecimal`
  * `weekend_rate` - `BigDecimal`
  
* [ ] Define `Guest` model (Day 1 Evening - 20min)
  * `guest_id` - `int`
  * `first_name` - `String`
  * `last_name` - `String`
  * `email` - `String`
  * `phone` - `String`
  * `state` - `String`
  
* [ ] Define `Reservation` model (Day 1 Evening - 20min)
  * ** UUID Identifier.csv UNKNOWN - Generate this...apply it to .csv title  **
  * `id` - `int` ** Use nextId to generate this aside from the UUID 
  * `start_date` - `LocalDate`
  * `end_date` - `LocalDate`
  * `guest_id` - `int`
  * `total` - `BigDecimal`
  
#### Data Layer
* Package: learn.house.data
* Classes
  * `DataAccessException`
  * `HostFileRepository`
  * `GuestFileRepository`
  * `ReservationFileRepository`
* Interface
  * `Host Repository`
  * `Guest Repository`
  * `Reservation Repository`
* Testing
  * `HostFileRepositoryTest`
  * `GuestFileRepositoryTest`
  * `ReservationFileRepositoryTest`
  * `HostRepositoryDouble`
  * `GuestRepositoryDouble`
  * `ReservationRepositoryDouble`
  
#### Domain Layer
* Package: learn.house.domain
* Classes
  * `HostService`
  * `GuestService`
  * `ReservationService`
* Testing
  * `HostServiceTest`
  * `GuestServiceTest`
  * `ReservationServiceTest`
  
#### UI Layer
* Package: learn.house.ui
* Classes
  * `ConsoleIO`
  * `Controller`
  * `View`
* Enumeration
  * `MainMenuOption`
  
### Steps 
##### View Reservations By Host
* Data Layer `(Day 1 evening ~1.5 hrs, into Day 2 morning potentially ~1.5 hrs)`
  * [ ] Build methods in corresponding file repo's and double 
  * [ ] Test methods built, both happy and unhappy paths 
* Domain Layer `(Day 2 morning - 2 hrs)`
  * [ ] Build methods in corresponding services
  * [ ] Test methods built, both happy and unhappy paths
* Ui layer `(Day 2 Afternoon - 3hrs)`
  * [ ] Build methods in corresponding controller/view
  * [ ] Build corresponding helper methods in consoleIO
  * [ ] Build Main Menu methods/run methods 
* App Class `(Day 2 afternoon - 1 hr)`
  * [ ] Implement Spring DI (Annotation) throughout code
  * [ ] Run app and test the menu navigates properly 

##### Create a Reservation
* Data Layer `(Day 2 evening - 3 hrs)`
  * [ ] Build methods in corresponding file repo's and double
  * [ ] Test methods built, both happy and unhappy paths
* Domain Layer `(Day 3 morning - 3 hrs)`
  * [ ] Build methods in corresponding services
  * [ ] Implement validation methods
  * [ ] Test methods built, both happy and unhappy paths
* Ui layer `(Day 3 morning/afternoon - 2 hrs)`
  * [ ] Build methods in corresponding controller/view
  * [ ] Build corresponding helper methods in consoleIO
  * [ ] Build Main Menu methods/run methods 

##### Edit an Existing Reservation
* Data Layer `(Day 3 afternoon - 3 hrs)`
  * [ ] Build methods in corresponding file repo's and double
  * [ ] Test methods built, both happy and unhappy paths
* Domain Layer `(Day 3 evening - 3 hrs)`
  * [ ] Build methods in corresponding services
  * [ ] Implement validation methods
  * [ ] Test methods built, both happy and unhappy paths
* Ui layer `(Day 4 morning/afternoon - 4 hrs)`
  * [ ] Build methods in corresponding controller/view
  * [ ] Build corresponding helper methods in consoleIO
  * [ ] Build Main Menu methods/run methods 

##### Cancel an Existing Reservation
* Data Layer `(Day 4 afternoon - 2 hrs)`
  * [ ] Build methods in corresponding file repo's and double
  * [ ] Test methods built, both happy and unhappy paths
* Domain Layer `(Day 4 afternoon/evening - 2 hrs)`
  * [ ] Build methods in corresponding services
  * [ ] Implement validation methods
  * [ ] Test methods built, both happy and unhappy paths
* Ui layer `(Day 5 morning - 1.5 hrs)`
  * [ ] Build methods in corresponding controller/view
  * [ ] Build corresponding helper methods in consoleIO
  * [ ] Build Main Menu methods/run methods 
  
* [ ] Run through App and test all validations (Day 5 morning/afternoon - 2 hrs)
* [ ] Tidy code and UI `(Day 5 afternoon - 1 hr)`

Stretch goals over the weekend if I follow this schedule and MVP is done
   

  
  