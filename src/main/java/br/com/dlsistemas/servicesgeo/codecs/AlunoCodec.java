package br.com.dlsistemas.servicesgeo.codecs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import br.com.dlsistemas.servicesgeo.models.Aluno;
import br.com.dlsistemas.servicesgeo.models.Contato;
import br.com.dlsistemas.servicesgeo.models.Curso;
import br.com.dlsistemas.servicesgeo.models.Habilidade;
import br.com.dlsistemas.servicesgeo.models.Nota;

public class AlunoCodec implements CollectibleCodec<Aluno> {

	private Codec<Document> codec;

	public AlunoCodec(Codec<Document> codec) {
		this.codec = codec;
	}

	@Override
	public void encode(BsonWriter writer, Aluno aluno, EncoderContext encoder) {
		ObjectId id = aluno.getId();
		String nome = aluno.getNome();
		Date dataNascimento = aluno.getDataNascimento();
		Curso curso = aluno.getCurso();
		List<Habilidade> habilidades = aluno.getHabilidades();
		List<Nota> notas = aluno.getNotas();
		Contato contato = aluno.getContato();

		Document document = new Document();

		List<Double> coordinates = new ArrayList<Double>();

		for (Double location : contato.getCoordinates()) {
			coordinates.add(location);
		}

		document.put("_id", id);
		document.put("nome", nome);
		document.put("data_nascimento", dataNascimento);
		document.put("curso", new Document("nome", curso.getNome()));

		if (habilidades != null) {
			List<Document> habilidadesDocument = new ArrayList<>();

			for (Habilidade habilidade : habilidades) {
				habilidadesDocument
						.add(new Document("nome", habilidade.getNome()).append("nivel", habilidade.getNivel()));
			}

			document.put("habilidades", habilidadesDocument);
		}

		if (notas != null) {
			List<Double> notasParaSalvar = new ArrayList<>();

			for (Nota nota : notas) {
				notasParaSalvar.add(nota.getValor());
			}
			document.put("notas", notasParaSalvar);
		}

		document.put("contato", new Document().append("endereco", contato.getEndereco())
				.append("coordinates", coordinates).append("type", contato.getType()));

		codec.encode(writer, document, encoder);

	}

	@Override
	public Class<Aluno> getEncoderClass() {
		return Aluno.class;
	}

	@Override
	public Aluno decode(BsonReader reader, DecoderContext decoder) {
		Document document = codec.decode(reader, decoder);
		Aluno aluno = new Aluno();

		aluno.setId(document.getObjectId("_id"));
		aluno.setNome(document.getString("nome"));
		aluno.setDataNascimento(document.getDate("data_nascimento"));
		Document curso = (Document) document.get("curso");

		if (curso != null) {
			String nomeCurso = curso.getString("nome");
			aluno.setCurso(new Curso(nomeCurso));
		}

		List<Double> notas = (List<Double>) document.get("notas");

		if (notas != null) {
			List<Nota> notasAluno = new ArrayList<>();
			for (Double nota : notas) {
				notasAluno.add(new Nota(nota));
				aluno.setNotas(notasAluno);
			}
		}

		List<Document> habilidades = (List<Document>) document.get("habilidades");

		if (habilidades != null) {
			List<Habilidade> habilidadesAluno = new ArrayList<>();
			for (Document documentHabilidade : habilidades) {
				habilidadesAluno.add(
						new Habilidade(documentHabilidade.getString("nome"), documentHabilidade.getString("nivel")));
			}
			aluno.setHabilidades(habilidadesAluno);
		}

		Document contato = (Document) document.get("contato");

		if (contato != null) {
			String endereco = contato.getString("endereco");
			List<Double> coordinates = (List<Double>) contato.get("coordinates");
			aluno.setContato(new Contato(endereco, coordinates));
		}
		return aluno;
	}

	@Override
	public Aluno generateIdIfAbsentFromDocument(Aluno aluno) {
		return documentHasId(aluno) ? aluno.criarId() : aluno;
	}

	@Override
	public boolean documentHasId(Aluno aluno) {
		return aluno.getId() == null;
	}

	@Override
	public BsonValue getDocumentId(Aluno aluno) {
		if (!documentHasId(aluno)) {
			throw new IllegalStateException("Esse Document não tem id");
		}
		return new BsonString(aluno.getId().toHexString());
	}

}
