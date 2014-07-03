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

	static Form<Usuario> metaForm = Form.form(Usuario.class);
	private static GenericDAO dao = new GenericDAOImpl();
    
	@Transactional
	public static Result index() {

		
		List<Usuario> result = getDao().findAllByClassName("Usuario");
		if(!result.isEmpty()){
			return ok(index.render(result.get(result.size() - 1).getNome()));
		}
		return ok(index.render("No previous user"));
    }
	
	@Transactional 
	public static Result tabela(){
		List<Hackfest> result = getDao().findAllByClassName("Hackfest");
		return ok(tabelaDeHackfests.render(result));
	}
    
    
    
    public static Result formularioNovoUsuario(){
    	Form<Usuario> form = Form.form(Usuario.class);
    	return ok(formularioNovoUsuario.render(form));
    }
    
    public static Result formularioNovoHackfest(){
    	Form<Hackfest> form = Form.form(Hackfest.class);
    	return ok(views.html.formularioNovoHackfest.render(form));
    }
    
    @Transactional
    public static Result confirmaPresenca(){
		return TODO;
    
    }
    
    @Transactional
    public static Result cadastraHackfest(){
    	//todas as metas do bd
    	Form<Hackfest> form = Form.form(Hackfest.class).bindFromRequest();
    	if(form.hasErrors()){
    		return badRequest(views.html.formularioNovoHackfest.render(form));
		}
		// Persiste o Livro criado
		getDao().persist(form.get());
		// Espelha no Banco de Dados
		getDao().flush();

		return redirect(routes.Application.index());
    }
    

   
    @Transactional
    public static Result cadastraUsuario(){
    	//todas as metas do bd
    	Form<Usuario> form = Form.form(Usuario.class).bindFromRequest();
    	if(form.hasErrors()){
    		return badRequest(formularioNovoUsuario.render(form));
		}
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
