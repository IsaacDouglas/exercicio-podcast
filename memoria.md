![imagem1](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/imagem1.png)

Quando inicia o App verifica que ele começa com aproximadamente 30MB como mostra a primeira imagem, mas quando passa bastante tempo com o app aberto ele logo abaixa para proximo de 25MB como na segunda imagem, isso deve ser relacionado ao esforço do android abrir a aplicação e logo depois ele volta ao normal.

![imagem2](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/imagem2.png)

Quando inicia o download de algum podcast percebe que a memoria sobe rapidamente e volta a descer, como mostra a imagem 3, isso acontece por que ele inicializa o ok http e outras funções responsaveis pela execução do download e logo apos desce e comeca a subir lentamente enquando baixa o arquivo.

![imagem3](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/imagem3.png)

Como mostra a imagem 4, logo após o download do episodio a memoria mais uma vez apresenta um pico, mas está relacionado pelo salvamento da uri no banco e atualização da view com a informação nova de trocar o botoão para Play

![imagem4](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/imagem4.png)

Com o tempo o android fica finalizando as referencias que nao estao sendo usadas como na imagem 5 e 6.

![imagem5](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/imagem5.png)
![imagem6](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/imagem6.png)