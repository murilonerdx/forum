package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import br.com.alura.forum.controller.dto.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.modelo.TopicoForm;
import br.com.alura.forum.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping(value="/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping()
	@Cacheable(value="listaDeTopicos")
	@CacheEvict(value="listaDeTopicos", allEntries = true)
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,
								 @RequestParam(value="pagina", required = false, defaultValue = "0") int pagina,
								 @RequestParam(value="qtd", required = false, defaultValue = "5") int qtd, @RequestParam(value="ordenacao", required = false, defaultValue = "id") String ordenacao) {

		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(PageRequest.of(pagina, qtd, Sort.Direction.ASC, ordenacao));
			return TopicoDto.converter(topicos);
		} else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, PageRequest.of(pagina, qtd, Sort.Direction.ASC, ordenacao));
			return TopicoDto.converter(topicos);
		}
	}

	@PostMapping()
	@Transactional
	@CacheEvict(value="listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder component){
		Topico topico = topicoForm.converter(cursoRepository);
		topicoRepository.save(topico);

		URI uri = component.path("/topico/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		return topico.map(value -> ResponseEntity.ok(new DetalhesDoTopicoDto(value))).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.notFound().build();
	}


}










