import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class TelaFinanciamento extends JFrame {

    private static final double TAXA_FINANCIAMENTO = 0.10;

    private final JComboBox<String> comboMarca = new JComboBox<>();
    private final JTextField campoModelo = new JTextField(20);
    private final JComboBox<Integer> comboAno = new JComboBox<>();
    private final JTextField campoValorVeiculo = new JTextField(12);

    private final JRadioButton radioNovo = new JRadioButton("Novo");
    private final JRadioButton radioUsado = new JRadioButton("Usado");
    private final JTextField campoQuilometragem = new JTextField(12);
    private final JTextField campoProprietarios = new JTextField(12);

    private final JCheckBox checkPossuiEntrada = new JCheckBox("Possui entrada");
    private final JLabel rotuloEntrada = new JLabel("Valor da entrada:");
    private final JTextField campoEntrada = new JTextField(12);
    private final JComboBox<Integer> comboParcelas = new JComboBox<>(
            new Integer[] { 12, 24, 36, 48, 60 });

    private final JButton botaoLimpar = new JButton("Limpar");
    private final JButton botaoCalcular = new JButton("Calcular");

    private final JPanel painelConteudo = new JPanel();
    private JPanel painelVeiculoUsado;
    private JPanel painelResultado;
    private final JLabel rotuloValorFinanciado = new JLabel();
    private final JLabel rotuloValorParcela = new JLabel();
    private final JLabel rotuloTotalPagar = new JLabel();

    private double valorVeiculoValidado;
    private double valorEntradaValidado;

    public TelaFinanciamento() {
        super("Simulador de Financiamento de Veículos");
        inicializarComponentes();
        configurarEventos();
    }

    private void inicializarComponentes() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        preencherMarcas();
        preencherAnos();

        ButtonGroup grupoTipoVeiculo = new ButtonGroup();
        grupoTipoVeiculo.add(radioNovo);
        grupoTipoVeiculo.add(radioUsado);
        radioNovo.setSelected(true);

        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBorder(new EmptyBorder(10, 10, 10, 10));

        painelConteudo.add(criarPainelVeiculo());
        painelVeiculoUsado = criarPainelVeiculoUsado();
        painelConteudo.add(painelVeiculoUsado);
        painelConteudo.add(criarPainelFinanciamento());
        painelConteudo.add(criarPainelBotoes());
        painelResultado = criarPainelResultado();
        painelConteudo.add(painelResultado);

        add(painelConteudo, BorderLayout.CENTER);

        painelVeiculoUsado.setVisible(false);
        rotuloEntrada.setVisible(false);
        campoEntrada.setVisible(false);
        painelResultado.setVisible(false);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void preencherMarcas() {
        comboMarca.addItem("Selecione...");
        comboMarca.addItem("Chevrolet");
        comboMarca.addItem("Fiat");
        comboMarca.addItem("Ford");
        comboMarca.addItem("Honda");
        comboMarca.addItem("Hyundai");
        comboMarca.addItem("Jeep");
        comboMarca.addItem("Renault");
        comboMarca.addItem("Toyota");
        comboMarca.addItem("Volkswagen");
    }

    private void preencherAnos() {
        for (int ano = 2026; ano >= 2000; ano--) {
            comboAno.addItem(ano);
        }
    }

    private JPanel criarPainelVeiculo() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Dados do Veículo"));
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints restricoes = criarRestricoes();

        adicionarCampo(painel, restricoes, 0, "Marca:", comboMarca);
        adicionarCampo(painel, restricoes, 1, "Modelo:", campoModelo);
        adicionarCampo(painel, restricoes, 2, "Ano:", comboAno);
        adicionarCampo(painel, restricoes, 3, "Valor do veículo:", campoValorVeiculo);

        restricoes.gridx = 0;
        restricoes.gridy = 4;
        painel.add(new JLabel("Tipo do veículo:"), restricoes);

        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painelTipo.add(radioNovo);
        painelTipo.add(radioUsado);
        restricoes.gridx = 1;
        painel.add(painelTipo, restricoes);

        return painel;
    }

    private JPanel criarPainelVeiculoUsado() {
        JPanel painel = new JPanel(new GridLayout(2, 2, 8, 8));
        painel.setBorder(BorderFactory.createTitledBorder("Dados do Veículo Usado"));
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);

        painel.add(new JLabel("Quilometragem:"));
        painel.add(campoQuilometragem);
        painel.add(new JLabel("Quantidade de proprietários:"));
        painel.add(campoProprietarios);

        return painel;
    }

    private JPanel criarPainelFinanciamento() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Financiamento"));
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints restricoes = criarRestricoes();

        restricoes.gridx = 0;
        restricoes.gridy = 0;
        restricoes.gridwidth = 2;
        painel.add(checkPossuiEntrada, restricoes);

        restricoes.gridwidth = 1;
        restricoes.gridy = 1;
        restricoes.gridx = 0;
        painel.add(rotuloEntrada, restricoes);
        restricoes.gridx = 1;
        painel.add(campoEntrada, restricoes);

        adicionarCampo(painel, restricoes, 2, "Número de parcelas:", comboParcelas);

        return painel;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.add(botaoLimpar);
        painel.add(botaoCalcular);
        return painel;
    }

    private JPanel criarPainelResultado() {
        JPanel painel = new JPanel(new GridLayout(3, 1, 5, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Resultado da Simulação"));
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.add(rotuloValorFinanciado);
        painel.add(rotuloValorParcela);
        painel.add(rotuloTotalPagar);
        return painel;
    }

    private GridBagConstraints criarRestricoes() {
        GridBagConstraints restricoes = new GridBagConstraints();
        restricoes.insets = new Insets(4, 4, 4, 4);
        restricoes.anchor = GridBagConstraints.WEST;
        restricoes.fill = GridBagConstraints.HORIZONTAL;
        return restricoes;
    }

    private void adicionarCampo(JPanel painel, GridBagConstraints restricoes,
            int linha, String textoRotulo, Component componente) {
        restricoes.gridy = linha;
        restricoes.gridx = 0;
        restricoes.weightx = 0;
        painel.add(new JLabel(textoRotulo), restricoes);

        restricoes.gridx = 1;
        restricoes.weightx = 1;
        painel.add(componente, restricoes);
    }

    private void configurarEventos() {
        radioNovo.addActionListener(evento -> atualizarVisibilidadePainelUsado());
        radioUsado.addActionListener(evento -> atualizarVisibilidadePainelUsado());
        checkPossuiEntrada.addActionListener(evento -> atualizarVisibilidadeEntrada());
        botaoLimpar.addActionListener(evento -> limparFormulario());
        botaoCalcular.addActionListener(evento -> {
            if (validarCampos()) {
                calcularFinanciamento();
            }
        });
    }

    private void atualizarVisibilidadePainelUsado() {
        painelVeiculoUsado.setVisible(radioUsado.isSelected());
        atualizarJanela();
    }

    private void atualizarVisibilidadeEntrada() {
        boolean mostrarEntrada = checkPossuiEntrada.isSelected();
        rotuloEntrada.setVisible(mostrarEntrada);
        campoEntrada.setVisible(mostrarEntrada);
        atualizarJanela();
    }

    private boolean validarCampos() {
        painelResultado.setVisible(false);

        if (comboMarca.getSelectedIndex() == 0) {
            return mostrarErro("Selecione a marca do veículo.", comboMarca);
        }

        if (campoModelo.getText().trim().isEmpty()) {
            return mostrarErro("Informe o modelo do veículo.", campoModelo);
        }

        try {
            valorVeiculoValidado = converterNumero(campoValorVeiculo.getText());
            if (!numeroValido(valorVeiculoValidado) || valorVeiculoValidado <= 0) {
                return mostrarErro("Informe um valor válido e maior que zero para o veículo.",
                        campoValorVeiculo);
            }
        } catch (NumberFormatException excecao) {
            return mostrarErro("Informe um valor numérico válido para o veículo.",
                    campoValorVeiculo);
        }

        if (radioUsado.isSelected()) {
            if (!validarDadosVeiculoUsado()) {
                return false;
            }
        }

        valorEntradaValidado = 0;
        if (checkPossuiEntrada.isSelected()) {
            try {
                valorEntradaValidado = converterNumero(campoEntrada.getText());
                if (!numeroValido(valorEntradaValidado) || valorEntradaValidado < 0) {
                    return mostrarErro("Informe um valor de entrada válido e não negativo.",
                            campoEntrada);
                }
                if (valorEntradaValidado >= valorVeiculoValidado) {
                    return mostrarErro("A entrada deve ser menor que o valor do veículo.",
                            campoEntrada);
                }
            } catch (NumberFormatException excecao) {
                return mostrarErro("Informe um valor numérico válido para a entrada.",
                        campoEntrada);
            }
        }

        return true;
    }

    private boolean validarDadosVeiculoUsado() {
        try {
            double quilometragem = converterNumero(campoQuilometragem.getText());
            if (!numeroValido(quilometragem) || quilometragem < 0) {
                return mostrarErro("Informe uma quilometragem válida e não negativa.",
                        campoQuilometragem);
            }
        } catch (NumberFormatException excecao) {
            return mostrarErro("Informe uma quilometragem numérica válida.",
                    campoQuilometragem);
        }

        try {
            int quantidadeProprietarios = Integer.parseInt(campoProprietarios.getText().trim());
            if (quantidadeProprietarios < 0) {
                return mostrarErro("Informe uma quantidade de proprietários não negativa.",
                        campoProprietarios);
            }
        } catch (NumberFormatException excecao) {
            return mostrarErro("Informe um número inteiro válido de proprietários.",
                    campoProprietarios);
        }

        return true;
    }

    private double converterNumero(String texto) throws NumberFormatException {
        String numero = texto.trim();
        if (numero.isEmpty()) {
            throw new NumberFormatException();
        }

        if (numero.contains(",")) {
            numero = numero.replace(".", "").replace(",", ".");
        }

        return Double.parseDouble(numero);
    }

    private boolean numeroValido(double numero) {
        return !Double.isNaN(numero) && !Double.isInfinite(numero);
    }

    private boolean mostrarErro(String mensagem, Component campo) {
        JOptionPane.showMessageDialog(this, mensagem, "Dados inválidos",
                JOptionPane.WARNING_MESSAGE);
        campo.requestFocusInWindow();
        return false;
    }

    private void calcularFinanciamento() {
        int numeroParcelas = (Integer) comboParcelas.getSelectedItem();
        double valorFinanciado = valorVeiculoValidado - valorEntradaValidado;
        double valorTotal = valorFinanciado * (1 + TAXA_FINANCIAMENTO);
        double valorParcela = valorTotal / numeroParcelas;
        double totalPagar = valorParcela * numeroParcelas;

        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        rotuloValorFinanciado.setText(
                "Valor financiado: " + formatoMoeda.format(valorFinanciado));
        rotuloValorParcela.setText(
                "Valor da parcela: " + formatoMoeda.format(valorParcela));
        rotuloTotalPagar.setText(
                "Total a pagar: " + formatoMoeda.format(totalPagar));

        painelResultado.setVisible(true);
        atualizarJanela();
    }

    private void limparFormulario() {
        comboMarca.setSelectedIndex(0);
        campoModelo.setText("");
        comboAno.setSelectedIndex(0);
        campoValorVeiculo.setText("");

        radioNovo.setSelected(true);
        campoQuilometragem.setText("");
        campoProprietarios.setText("");
        painelVeiculoUsado.setVisible(false);

        checkPossuiEntrada.setSelected(false);
        campoEntrada.setText("");
        rotuloEntrada.setVisible(false);
        campoEntrada.setVisible(false);
        comboParcelas.setSelectedIndex(0);

        rotuloValorFinanciado.setText("");
        rotuloValorParcela.setText("");
        rotuloTotalPagar.setText("");
        painelResultado.setVisible(false);

        valorVeiculoValidado = 0;
        valorEntradaValidado = 0;
        campoModelo.requestFocusInWindow();
        atualizarJanela();
    }

    private void atualizarJanela() {
        painelConteudo.revalidate();
        painelConteudo.repaint();
        pack();
    }
}
