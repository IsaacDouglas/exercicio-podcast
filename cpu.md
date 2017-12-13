## CPU & Performance

Logo quando inicializa o App o processamento some, mas se deve ao fato de inicializar a MainActivity com as partes grafica e também a inicialização do ok http para fazer a conexão com a internet como mostra na imagem abaixo.

![cpu1.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/cpu1.png)
*Imagem 1*

Após algum tempo com o app aberto ele fica geralmente abaixo dos 5% de consumo do processador, como mostra na segunda imagem.

![cpu2.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/cpu2.png)
*Imagem 2*

Quando o download começa o consumo do processador dar um pico, onde aparece o IntentService nas informações, que é onde é feito o download em segundo plano como na imagem 3 mostra.

![cpu3.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/cpu3.png)
*Imagem 3*

Enquanto o download é executado o processamento fica um pouco mais alto fica em torno abaixo dos 10%, mas quando o download acaba o processamento apresenta um pico que é na hora de gravar no banco a URI atualizar a interface e encerras o IntentService como mostra as imagens 4 e 5.

![cpu4.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/cpu4.png)
*Imagem 4*
![cpu5.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/cpu5.png)
*Imagem 5*

Logo quando inicializa a música o processamento sobe um pouco por causa da atualização da tela por causa do estado do botão, e inicializa o MediaPlayer e logo quando coloca pausa segue o mesmo padrão sobe um pouco e baixa depois, por causa da finalização do MediaPlayer e salvar a posição da pausa no banco de dados, como mostra nas imagens 6 e 7.

![cpu6.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/cpu6.png)
*Imagem 6*
![cpu7.png](https://github.com/IsaacDouglas/exercicio-podcast/tree/master/imagens/cpu7.png)
*Imagem 7*