# IDwallTeste
Desafio IDdog https://iddog-api.now.sh

Aplicativo que utiliza autenticação por email e lista lindos cachorrinhos

## Arquitetura
* MVVM + Android Architecture Components
* Kodein - Kotlin Dependency Injection
* Retrofit
* Picasso
* Test Mockito + Mockwebserver

## Tela de Autenticação

Contem uma simples animação no inicio e basta informar seu email, o mesmo é validado antes de efetuar a requisição `/signup`

## Tela de Feed

Utiliza um `ViewPager` para listar os 4 tipos de cachorros — `husky`, `hound`, `pug` e `labrador`.
Caso ocorra algum problema na requisição é possivel tentar novamente apertando o botão `Try Again`

## Tela de Detalhe Feed

Imagem amplificada com possibilidade de zoom
