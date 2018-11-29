package br.com.dlsistemas.servicesgeo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.dlsistemas.servicesgeo.models.Aluno;
import br.com.dlsistemas.servicesgeo.repositorys.AlunoRepository;

@Controller
public class GeolocalizacaoController {

	@Autowired
	AlunoRepository alunoRepository;

	@GetMapping("/geolocalizacao/iniciarpesquisa")
	public String inicializarPesquisa(Model model) {		
		List<Aluno> alunos = alunoRepository.ObterTodosAlunos();
		model.addAttribute("alunos", alunos);				
		return "geolocalizacao/pesquisar";
	};

	
	@GetMapping("/geolocalizacao/pesquisar")
	public String pesquisar(@RequestParam("alunoId") String alunoId, Model model ) {
		
		Aluno aluno = alunoRepository.obterAlunoPor(alunoId);
		List<Aluno> alunosProximos = alunoRepository.pesquisarPorGeoLocalizacao(aluno);
		model.addAttribute("alunosProximos", alunosProximos);
		

		
		return "geolocalizacao/pesquisar";	
	}
}
