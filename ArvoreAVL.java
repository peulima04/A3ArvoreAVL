// Classe que representa um nó da árvore AVL
class No {
    int chave;
    int altura;
    No esquerdo;
    No direito;

    No(int chave) {
        this.chave = chave;
        this.altura = 1;
    }
}

// Classe que representa a árvore AVL
public class ArvoreAVL {
    private No raiz;
    private int contadorRotacoes = 0;

    // Retorna a altura de um nó
    private int altura(No no) {
        return (no == null) ? 0 : no.altura;
    }

    // Calcula o fator de balanceamento
    private int fatorBalanceamento(No no) {
        return (no == null) ? 0 : altura(no.esquerdo) - altura(no.direito);
    }

    // Rotação simples para a direita
    private No rotacaoDireita(No y) {
        contadorRotacoes++;
        No x = y.esquerdo;
        No T2 = x.direito;

        x.direito = y;
        y.esquerdo = T2;

        y.altura = Math.max(altura(y.esquerdo), altura(y.direito)) + 1;
        x.altura = Math.max(altura(x.esquerdo), altura(x.direito)) + 1;

        return x;
    }

    // Rotação simples para a esquerda
    private No rotacaoEsquerda(No x) {
        contadorRotacoes++;
        No y = x.direito;
        No T2 = y.esquerdo;

        y.esquerdo = x;
        x.direito = T2;

        x.altura = Math.max(altura(x.esquerdo), altura(x.direito)) + 1;
        y.altura = Math.max(altura(y.esquerdo), altura(y.direito)) + 1;

        return y;
    }

    // Inserção de um nó na árvore
    private No inserir(No no, int chave) {
        if (no == null) {
            return new No(chave);
        }

        if (chave < no.chave) {
            no.esquerdo = inserir(no.esquerdo, chave);
        } else if (chave > no.chave) {
            no.direito = inserir(no.direito, chave);
        } else {
            return no; // Não permite valores duplicados
        }

        no.altura = 1 + Math.max(altura(no.esquerdo), altura(no.direito));

        int balanceamento = fatorBalanceamento(no);

        // LL
        if (balanceamento > 1 && chave < no.esquerdo.chave) {
            return rotacaoDireita(no);
        }

        // RR
        if (balanceamento < -1 && chave > no.direito.chave) {
            return rotacaoEsquerda(no);
        }

        // LR
        if (balanceamento > 1 && chave > no.esquerdo.chave) {
            no.esquerdo = rotacaoEsquerda(no.esquerdo);
            return rotacaoDireita(no);
        }

        // RL
        if (balanceamento < -1 && chave < no.direito.chave) {
            no.direito = rotacaoDireita(no.direito);
            return rotacaoEsquerda(no);
        }

        return no;
    }

    public void inserir(int chave) {
        raiz = inserir(raiz, chave);
    }

    // Busca por uma chave
    public boolean buscar(int chave) {
        return buscar(raiz, chave);
    }

    private boolean buscar(No no, int chave) {
        if (no == null) {
            return false;
        }
        if (chave == no.chave) {
            return true;
        } else if (chave < no.chave) {
            return buscar(no.esquerdo, chave);
        } else {
            return buscar(no.direito, chave);
        }
    }

    // Encontra o nó com menor valor
    private No minimoValor(No no) {
        No atual = no;
        while (atual.esquerdo != null) {
            atual = atual.esquerdo;
        }
        return atual;
    }

    // Remove um nó da árvore
    private No remover(No no, int chave) {
        if (no == null) {
            return no;
        }

        if (chave < no.chave) {
            no.esquerdo = remover(no.esquerdo, chave);
        } else if (chave > no.chave) {
            no.direito = remover(no.direito, chave);
        } else {
            if ((no.esquerdo == null) || (no.direito == null)) {
                No temp = (no.esquerdo != null) ? no.esquerdo : no.direito;
                if (temp == null) {
                    temp = no;
                    no = null;
                } else {
                    no = temp;
                }
            } else {
                No temp = minimoValor(no.direito);
                no.chave = temp.chave;
                no.direito = remover(no.direito, temp.chave);
            }
        }

        if (no == null) {
            return no;
        }

        no.altura = Math.max(altura(no.esquerdo), altura(no.direito)) + 1;

        int balanceamento = fatorBalanceamento(no);

        // LL
        if (balanceamento > 1 && fatorBalanceamento(no.esquerdo) >= 0) {
            return rotacaoDireita(no);
        }

        // LR
        if (balanceamento > 1 && fatorBalanceamento(no.esquerdo) < 0) {
            no.esquerdo = rotacaoEsquerda(no.esquerdo);
            return rotacaoDireita(no);
        }

        // RR
        if (balanceamento < -1 && fatorBalanceamento(no.direito) <= 0) {
            return rotacaoEsquerda(no);
        }

        // RL
        if (balanceamento < -1 && fatorBalanceamento(no.direito) > 0) {
            no.direito = rotacaoDireita(no.direito);
            return rotacaoEsquerda(no);
        }

        return no;
    }

    public void remover(int chave) {
        raiz = remover(raiz, chave);
    }

    // Impressão em pré-ordem
    public void imprimirPreOrdem() {
        imprimirPreOrdem(raiz);
        System.out.println();
    }

    private void imprimirPreOrdem(No no) {
        if (no != null) {
            System.out.print(no.chave + " ");
            imprimirPreOrdem(no.esquerdo);
            imprimirPreOrdem(no.direito);
        }
    }

    // Exibir visualmente a árvore
    public void exibirEstrutura() {
        exibirEstrutura(raiz, "", true);
    }

    private void exibirEstrutura(No no, String prefixo, boolean ehFilhoDireito) {
        if (no != null) {
            System.out.println(prefixo + (ehFilhoDireito ? "└── " : "├── ") + no.chave);
            exibirEstrutura(no.direito, prefixo + (ehFilhoDireito ? "    " : "│   "), false);
            exibirEstrutura(no.esquerdo, prefixo + (ehFilhoDireito ? "    " : "│   "), true);
        }
    }

    // Retorna o número de rotações realizadas
    public int getContadorRotacoes() {
        return contadorRotacoes;
    }

    // Método principal
    public static void main(String[] args) {
        ArvoreAVL arvore = new ArvoreAVL();

        // Inserindo elementos
        arvore.inserir(10);
        arvore.inserir(20);
        arvore.inserir(30);
        arvore.inserir(40);
        arvore.inserir(50);
        arvore.inserir(25);

        System.out.println("\nÁrvore em pré-ordem após inserções:");
        arvore.imprimirPreOrdem();

        System.out.println("\nEstrutura da Árvore:");
        arvore.exibirEstrutura();

        // Buscando valores
        System.out.println("\nBusca por 30: " + arvore.buscar(30));
        System.out.println("Busca por 60: " + arvore.buscar(60));

        // Removendo um elemento
        arvore.remover(40);

        System.out.println("\nÁrvore em pré-ordem após remover 40:");
        arvore.imprimirPreOrdem();

        System.out.println("\nEstrutura da Árvore após remoção:");
        arvore.exibirEstrutura();

        // Mostrando o total de rotações realizadas
        System.out.println("\nNúmero de rotações realizadas: " + arvore.getContadorRotacoes());
    }
}
