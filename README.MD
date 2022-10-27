# Mercado Pago - Payment App

## Project Composition

**Clean architecture** is the main architecture for this project.
Unidirectional Data Flow **MVI** in presentation layer

## Unit Tests

Implemented unit tests for data and presentation layer

Data layer
 - Repository
 - Remote implementation
 
Presentation layer
- Mapper
- Processor
- Reducer
- ViewModel

## Diagram

In progress...

### Dependencies

- Retrofit
- Coroutines
- Dagger
- MockK
- Glide

### Libraries

Created two libraries for MVI flow and ui components reusables

- Payment ui components: [PaymentUiComponents](https://github.com/LepeFelipe/PaymentUiComponentsApp)
- Payment Mvi: [PaymentMvi](https://github.com/LepeFelipe/PaymentUiComponentsApp)

### Work Boards

- Figma: [Payment Flow](https://www.figma.com/file/ctU57yyEDnnKiE6cZ3aAZZ/Mercado-Pago---Payment-App-Test?node-id=0%3A1)
- MURAL (Technical User Story Mapping): [USDMT](https://app.mural.co/t/flepe8742/m/flepe8742/1666323323276/b4a04d77d545a46a657e6dc17765979010560232?sender=u91bfdf2278077d9fe7763616)

### Apks

- Develop [Dev](https://github.com/LepeFelipe/PaymentApp/blob/develop/payment-app/dev/debug/payment-app-dev-debug.apk)
- Production [Prod](https://github.com/LepeFelipe/PaymentApp/blob/develop/payment-app/prod/debug/payment-app-prod-debug.apk)