package controllers;

import java.util.List;

import models.Hackfest;
import models.Usuario;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.formularioNovoUsuario;
import views.html.index;
import views.html.tabelaDeHackfests;

public class Application extends Controller {

	static GenericDAO dao = new GenericDAOImpl();
	static Form<Usuario> usuarioForm = Form.form(Usuario.class);
	static Form<Hackfest> hackForm = Form.form(Hackfest.class);	
	static Usuario usuarioAtual;

	@Transactional
	public static Result index() {
		
		List<Usuario> result = getDao().findAllByClassName("Usuario");
		if (result.isEmpty()) {
			return ok(formularioNovoUsuario.render(usuarioForm, "Empty"));
		}
		
		usuarioAtual = result.get(result.size() - 1);
		return ok(index.render(usuarioAtual.getNome()));
	}

	@Transactional
	public static Result tabela() {
		List<Hackfest> result = getDao().findAllByClassName("Hackfest");
		return ok(tabelaDeHackfests.render(result, usuarioAtual.getNome()));
	}

	public static Result formularioNovoUsuario() {
		usuarioForm = Form.form(Usuario.class);
		return ok(formularioNovoUsuario.render(usuarioForm,
				usuarioAtual.getNome()));
	}

	public static Result formularioNovoHackfest() {
		hackForm = Form.form(Hackfest.class);
		return ok(views.html.formularioNovoHackfest.render(hackForm,
				usuarioAtual.getNome()));
	}

	@Transactional
	public static Result confirmaPresenca(Long id) {
		Hackfest hack = new Hackfest();
		hack = getDao().findByEntityId(Hackfest.class, id);
		hack.addUsuario(usuarioAtual);
		dao.merge(hack);
		dao.flush();
		return  ok(index.render(usuarioAtual.getNome()));

	}

	@Transactional
	public static Result cadastraHackfest() {
		// todas as metas do bd
		Form<Hackfest> form = hackForm.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(views.html.formularioNovoHackfest.render(form,
					usuarioAtual.getNome()));
		}

		// Persiste o Livro criado
		getDao().persist(form.get());
		// Espelha no Banco de Dados
		getDao().flush();

		return redirect(routes.Application.index());
	}

	@Transactional
	public static Result cadastraUsuario() {
		// todas as metas do bd
		Form<Usuario> form = usuarioForm.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(formularioNovoUsuario.render(form,
					usuarioAtual.getNome()));
		}

		usuarioAtual = form.get();
		// Persiste o Livro criado
		getDao().persist(form.get());
		// Espelha no Banco de Dados
		getDao().flush();

		return redirect(routes.Application.index());
	}

	public static GenericDAO getDao() {
		return dao;
	}

	public static void setDao(GenericDAO dao) {
		Application.dao = dao;
	}

}
