package org.superbiz.moviefun;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private MoviesBean moviesBean;

    public HomeController(MoviesBean moviesBean) {
        this.moviesBean = moviesBean;
    }

    @GetMapping("/")
    public String index (){
        return "index";
    }

    @GetMapping("/setup")
    public String setup(){
        moviesBean.addMovie(new Movie("Batman","Nolan","Action", 9, 2004));
        moviesBean.addMovie(new Movie("Batman1","Nolan1","Action", 8, 2005));
        moviesBean.addMovie(new Movie("Batman2","Nolan2","Action", 7, 2006));
        moviesBean.addMovie(new Movie("Batman3","Nolan3","Action", 6, 2007));
        moviesBean.addMovie(new Movie("Batman4","Nolan4","Action", 5, 2008));
        moviesBean.addMovie(new Movie("Batman5","Nolan5","Action", 4, 2009));
        moviesBean.addMovie(new Movie("Batman6","Nolan6","Action", 3, 2010));

        return "setup";
    }

}
