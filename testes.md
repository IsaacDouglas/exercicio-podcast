## Testes
#### Nos testes foram utilizadas as biblioteas Mockito e PowerMockito para realizar o mock das classes que são nativas do android.
### XmlParserTest
#### Pré configurando os testes.
##### Simulando um content antes do teste ser iniciado (Setup do teste). Inicialmente iremos utilizar uma lista de itens a serem parseados, e depois iremos verificar se eles vão ser parseados com sucesso.
    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        items.add(new ItemFeed("StarWars", "lucasfilm.com", "13/03/1995", "Lucas films - Star wars", "lucasfilm.com/download"));
        items.add(new ItemFeed("StarWars", "lucasfilm.com", "11/03/1995", "Lucas films - Star wars", "lucasfilm.com/download"));
        items.add(new ItemFeed("StarWars", "lucasfilm.com", "12/03/1995", "Lucas films - Star wars", "lucasfilm.com/download"));
    }
    
#### Iniciando teste de sucesso
##### O metodo mocka a classe de parser, pois o o XMLParser utiliza bibliotecas nativas do android. Em seguida simulamos um parseamento, e nos certificamos de que o resultado não foi nulo.
    @Test
    public void xmlFeedParserSuccessTest() throws IOException, XmlPullParserException{
        PowerMockito.mockStatic(XmlFeedParser.class);
        when(parserMock.parse(anyString())).thenReturn(items);
        List<ItemFeed> parser = XmlFeedParser.parse(FEED_URL);

        Assert.assertNotNull("Success", parser);
    }
    
    
#### Iniciando tesde com falha
##### Esse teste é o inverso do outro, para verificar quando o parser irá falhar.

    @Test
    public void xmlFeedParserFailTest() throws IOException, XmlPullParserException{
        PowerMockito.mockStatic(XmlFeedParser.class);

        when(parserMock.parse(anyString())).thenReturn(null);
        List<ItemFeed> parser = XmlFeedParser.parse("fail_feed");

        Assert.assertNull("Fail", parser);
    }

### DatabaseTest
#### Pré configurando os testes, simulamos um content type com dados fantasia.

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        content.put(PodcastProviderContract.TITLE, "StarWars");
        content.put(PodcastProviderContract.LINK, "www.lucasfilm.com/podcast");
        content.put(PodcastProviderContract.DATE, "13/03/1995");
        content.put(PodcastProviderContract.DESCRIPTION, "Lucas Films - StarWars");
        content.put(PodcastProviderContract.URI , "algumacoisaaqui");
    }

#### Iniciando teste de inserção com sucesso
##### O teste abaixo utiliza o content declarado no before, e simula uma inserção. O provider retorna um objeto, e nos certificamos de que o retorno não é nulo.
    @Test
    public void insertSuccess() throws Exception{
        when(provider.insert(uri, content)).thenReturn(success);
        doNothing().when(content).put(anyString(), anyString());

        Uri result = provider.insert(uri, content);

        Assert.assertNotNull("success", result);

    }
    
#### Iniciando teste com falha na inserção
##### O teste abaixo utiliza o mesmo content, mas ao inves de simular uma inserção, o mockito retorna null, para que aconteça a falha. Como o provider necessita do URI, e quando o metodo inser resulta em falha, ele retorna null, nos certificamos de que o retorno foi negativo.
    @Test
    public void insertFail() throws Exception {
        when(provider.insert(uri, content)).thenReturn(null);
        doNothing().when(content).put(anyString(), anyString());

        Uri result = provider.insert(uri, content);

        Assert.assertNull("fail", result);
    }
