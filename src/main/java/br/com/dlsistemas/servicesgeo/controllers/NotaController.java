package br.com.dlsistemas.servicesgeo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.dlsistemas.servicesgeo.models.Aluno;
import br.com.dlsistemas.servicesgeo.models.Nota;
import br.com.dlsistemas.servicesgeo.repositorys.AlunoRepository;

@Controller
public class NotaController {
	
	@Autowired
	private AlunoRepository repository;
	
	@GetMapping("/nota/cadastrar/{id}")
	public String cadastrar(@PathVariable String id, Model model ) {
		
		Aluno aluno = repository.obterAlunoPor(id);
		model.addAttribute("aluno", aluno);
		model.addAttribute("nota", new Nota());
		return "/nota/cadastrar";
	}

	@PostMapping("/nota/salvar/{id}")
	public String salvar(@PathVariable String id, @ModelAttribute Nota nota) {
		Aluno aluno = repository.obterAlunoPor(id);
		repository.salvar(aluno.adiciona(aluno, nota));		
		return "redirect:/aluno/listar";
	}
}
