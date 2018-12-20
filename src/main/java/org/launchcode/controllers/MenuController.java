package org.launchcode.controllers;


import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    MenuDao menuDao;

    @Autowired
    CheeseDao cheeseDao;


    // Request path: /category
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "My Menus");

        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddMenuForm(Model model) {


        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());

        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddMenuForm(@ModelAttribute @Valid Menu newMenu, Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }
        menuDao.save(newMenu);
        return "redirect:view/" + newMenu.getId();
    }

    @RequestMapping(value="view/{id}",method = RequestMethod.GET)
    public String viewMenu(@PathVariable int id , Model model){

        Menu menu = menuDao.findOne(id);

        model.addAttribute("menu",menu);
        return "menu/view";
    }
    @RequestMapping(value="add-item/{id}",method = RequestMethod.GET)
    public String displayAddItem(@PathVariable int id , Model model){

        Menu menu = menuDao.findOne(id);
        AddMenuItemForm form = new AddMenuItemForm(menu,cheeseDao.findAll());
        model.addAttribute("form",form);
        model.addAttribute("title","Add item to menu:" + menu.getName());

        return "menu/add-item";
    }

    @RequestMapping(value="add-item",method = RequestMethod.POST)
    public String processAddItem(@ModelAttribute @Valid AddMenuItemForm form,Errors errors,Model model){

        if (errors.hasErrors()) {
            model.addAttribute("form", form);
            model.addAttribute("title", "Add item to Menu:" + form.getMenuId());
            return "menu/add-item";
        }

        Menu menu  = menuDao.findOne(form.getMenuId());
        Cheese cheeseToAdd = cheeseDao.findOne(form.getCheeseId());
        menu.AddItem(cheeseToAdd);
        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }
}


