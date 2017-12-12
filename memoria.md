![imagem1.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/imagem1.png)
*Imagem 1*

Quando inicia o App verifica que ele começa com aproximadamente 30MB como mostra a primeira imagem, mas quando passa bastante tempo com o app aberto ele logo abaixa para próximo de 25MB como na segunda imagem, isso deve ser relacionado ao esforço do android abrir a aplicação e logo depois ele volta ao normal.

![imagem2.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/imagem2.png)
*Imagem 2*

Quando inicia o download de algum podcast percebe que a memória sobe rapidamente e volta a descer, como mostra a imagem 3, isso acontece porque ele inicializa o ok http e outras funções responsáveis pela execução do download e logo apos desce e começa a subir lentamente enquanto baixa o arquivo.

![imagem3.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/imagem3.png)
*Imagem 3*

Como mostra a imagem 4, logo após o download do episódio a memória mais uma vez apresenta um pico, mas está relacionado pelo salvamento da URI no banco e atualização da view com a informação nova de trocar o botão para Play

![imagem4.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/imagem4.png)
*Imagem 4*

Com o tempo o android fica finalizando as referências que nao estão sendo usadas como na imagem 5 e 6.

![imagem5.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/imagem5.png)
*Imagem 5*
![imagem6.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/imagem6.png)
*Imagem 6*

Quando dar play em um podcast a memoria logo sobe por causa da execução do MediaPlay, mas logo quando clica em pausar, essa memoria sobe mais um pouco, por causa do armazenamento do momento da pausa no banco e logo volta a descer, como mostra na imagem 7 e 8.

![imagem7.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/imagem7.png)
*Imagem 7*
![imagem8.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/imagem8.png)
*Imagem 8*
