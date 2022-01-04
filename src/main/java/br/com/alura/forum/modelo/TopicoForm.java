package br.com.alura.forum.modelo;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.repository.CursoRepository;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TopicoForm {

    @NotNull
    @NotEmpty(message="Titulo não pode estar vazio")@Min(5)
    private String titulo;

    @NotNull
    @NotEmpty(message="Mensagem não pode estar vazia")@Min(10)
    private String mensagem;
    private String nomeCurso;

    public TopicoForm(String titulo, String mensagem, String nomeCurso) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.nomeCurso = nomeCurso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public Topico converter(CursoRepository cursoRepository) {
        Curso curso = cursoRepository.findByNome(nomeCurso);
        return new Topico(this.titulo, this.mensagem, curso);
    }
}
