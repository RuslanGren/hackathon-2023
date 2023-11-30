# Hackathon-2023

### Available features without authentication:
1) Registration (POST /api/auth/register)

### There are two types of users:
1) Customer
2) Carrier

### Available features with authentication for Customer:
1) Add cargo (POST /api/customer/cargo/add) sends CargoDto
2) Delete cargo by id (DELETE /api/customer/cargo/delete) request param cargoId
3) Choose carrier for cargo (PATCH /api/customer/cargo/choose) request param cargoId and carrierResponseId 
4) Finish cargo and rate carrier (PATCH /api/customer/cargo/finish) request param cargoId and stars 1-5

### Available features with authentication for Carrier:
1) Add car (POST /api/carrier/add/car) sends CarDto
2) Add cars as list (POST /api/carrier/add/cars) sends list of CarDtos
3) Delete car (DELETE /api/carrier/delete/car) delete car by param carId
4) Response to cargo (PATCH /api/carrier/cargo/response) request param cargoId and request body of carrierResponseDto

The data is stored in a PostgreSQL database.
Password is stored as Hash
