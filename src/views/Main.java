package views;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import model.characters.*;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;

public class Main extends Application {

	//Instance Variable of Map & SelectableHeroes To Be Able To Update.
	GridPane mapGrid;
	ComboBox<String> selectableHeroes;
	VBox currAttr;
	Label healthBar;
	VBox SuppVacc;
	HBox currHero;
	HBox selHeroes;
	VBox selectableAttr;
	VBox Details;

	HBox heroesToSelect;

	static Scene startMenu;
	Scene mapLayout;
	Scene GameOver;
	Scene GameWin;

	@Override

	public void start(Stage PrimaryStage) throws Exception {
		HBox box = new HBox();
		Image LastLogo = new Image("views/pngfind.com-the-last-of-us-612846.png");

		ImageView LastLogo3= new ImageView(LastLogo);

		LastLogo3.setFitHeight(480);
		LastLogo3.setFitWidth(300);
		LastLogo3.setTranslateX(150);
		LastLogo3.setTranslateY(50);
		Image startGameBackground = new Image("views/Start 2.jpg");

		ImageView startGameBackgroundView = new ImageView(startGameBackground);
		startGameBackgroundView.setFitWidth(1000);
		startGameBackgroundView.setFitHeight(1000);

		startGameBackgroundView.fitWidthProperty().bind(PrimaryStage.widthProperty());
		startGameBackgroundView.fitHeightProperty().bind(PrimaryStage.heightProperty());


		Image buttonImage1 = new Image("views/Exit 1.png");

		// Create an ImageView for the button image
		ImageView buttonImageView1 = new ImageView(buttonImage1);
		buttonImageView1.setFitHeight(80);
		buttonImageView1.setFitWidth(215);

		buttonImageView1.setOnMouseClicked(e -> {
			PrimaryStage.close();
		});
		Image buttonImage = new Image("views/Start 3.png");

		// Create an ImageView for the button image
		ImageView buttonImageView = new ImageView(buttonImage);
		buttonImageView.setFitHeight(150);
		buttonImageView.setFitWidth(260);

		buttonImageView.setOnMouseClicked(e -> {
			try {
				startGameMenu(PrimaryStage);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		});



		VBox ExitButtonBox = new VBox(buttonImageView1);
		ExitButtonBox.setAlignment(Pos.BOTTOM_RIGHT);
		ExitButtonBox.setSpacing(10);
		ExitButtonBox.setTranslateX(1010);
		ExitButtonBox.setTranslateY(-25);

		VBox startButtonBox = new VBox(buttonImageView);
		startButtonBox.setAlignment(Pos.BOTTOM_LEFT);
		startButtonBox.setSpacing(10);
		startButtonBox.setTranslateX(20);
		startButtonBox.setTranslateY(0);


		VBox finalWallpaper=new VBox(LastLogo3);
		finalWallpaper.setAlignment(Pos.TOP_CENTER);
		finalWallpaper.setSpacing(10);


		box.getChildren().addAll(startButtonBox, ExitButtonBox,finalWallpaper);

		StackPane startMenuPane = new StackPane(startGameBackgroundView,box);

		startMenu = new Scene(startMenuPane, 1550, 790);

		PrimaryStage.setScene(startMenu);
		PrimaryStage.setTitle("The Last Of Us");
		PrimaryStage.show();

	}

	public  void startGameMenu(Stage PrimaryStage) throws IOException {
		Game.loadHeroes("Heroes.csv");

		HBox topMenu = new HBox();
		HBox centerMenu = new HBox();
		HBox bottomMenu = new HBox();
		HBox chaPic = new HBox();
		chaPic.setAlignment(Pos.CENTER_LEFT);
		topMenu.setAlignment(Pos.TOP_CENTER);
		centerMenu.setAlignment(Pos.CENTER);
		bottomMenu.setAlignment(Pos.BOTTOM_CENTER);
		centerMenu.setSpacing(400);
		Image image7 = new Image("views/Y4T9efU.jpg");


		//Font :
		String fontFamily = "VCR OSD Mono";
		Font font = Font.font(fontFamily, FontWeight.BOLD, 20);


		//Choose your character: 
		Image selectHero = new Image("views/ChooseCha.png");
		ImageView selectHeroView = new ImageView(selectHero);

		//Start Button
		Image startButton = new Image("views/Start 3.png");
		ImageView startButtonView = new ImageView(startButton);
		VBox startButtonBox = new VBox(startButtonView);

		startButtonBox.setAlignment(Pos.BOTTOM_LEFT);
		startButtonBox.setSpacing(10);
		startButtonBox.setTranslateX(20);

		//Details Box: 
		VBox detailsBox = new VBox();


		ComboBox<String> heroesList = new ComboBox<>();
		HashMap<String, Integer> hm = new HashMap<>();
		heroesList.setStyle("-fx-border-color: black;");
		heroesList.setStyle("-fx-background-color: white;");
		heroesList.setTranslateX(360);
		heroesList.setTranslateY(-70);


		int i = 0;

		for(Hero h : Game.availableHeroes) {
			heroesList.getItems().add(h.getName());
			hm.put(h.getName(), i);
			i++;
		}

		detailsBox.setAlignment(Pos.TOP_RIGHT);
		heroesList.setPromptText("Select Hero");

		heroesList.setOnAction(e -> {
			Hero hero = Game.availableHeroes.get(hm.get(heroesList.getValue()));
			Image image9=null ;
			if(detailsBox.getChildren() != null) {
				detailsBox.getChildren().clear();
			}
			if(chaPic.getChildren() != null) {
				chaPic.getChildren().clear();
			}
			if(hm.get(heroesList.getValue())==0) {
				image9 =new Image("views/JoelMillerPic3.png");
			}
			if(hm.get(heroesList.getValue())==1) {
				image9 =new Image("views/ElliesPic3.png");
			}
			if(hm.get(heroesList.getValue())==2) {
				image9 =new Image("views/TessPic3.png");
			}
			if(hm.get(heroesList.getValue())==3) {
				image9 =new Image("views/RileyPic3.png");
			}
			if(hm.get(heroesList.getValue())==4) {
				image9 =new Image("views/TommyMillerPic3.png");
			}
			if(hm.get(heroesList.getValue())==5) {
				image9 =new Image("views/BillPic33.png");
			}
			if(hm.get(heroesList.getValue())==6) {
				image9 =new Image("views/DavidPic3.png");
			}
			if(hm.get(heroesList.getValue())==7) {
				image9 =new Image("views/HenryBurellPic33.png");
			}

			ImageView imageView9=new ImageView(image9);
			imageView9.setFitHeight(300);
			imageView9.setFitWidth(350);
			imageView9.setTranslateX(100);
			imageView9.setTranslateY(60);
			imageView9.setStyle("-fx-border-radius: 50%; -fx-background-radius: 50%; -fx-border-color: black; -fx-border-width: 2px;");




			chaPic.getChildren().add(imageView9);
			chaPic.setTranslateX(20);






			Text type = new Text(getTypeOfHero(hero));

			Text ActionPoints = new Text("Action Points : "+hero.getActionsAvailable() + " ");
			Text AttackDmg = new Text("Attack Damage : " + hero.getAttackDmg() + " ");
			Text Health = new Text("Health : " + hero.getMaxHp() + " ");


			type.setFont(font);
			ActionPoints.setFont(font);
			AttackDmg.setFont(font);
			Health.setFont(font);

			type.setStyle(" -fx-font-family: VCR OSD Mono; -fx-font-size :25px; -fx-font-weight: bold;");
			ActionPoints.setStyle("-fx-font-size :25px; -fx-font-weight: bold;");
			AttackDmg.setStyle("-fx-font-size :25px; -fx-font-weight: bold;");
			Health.setStyle("-fx-font-size :25px; -fx-font-weight: bold;");
			detailsBox.getChildren().addAll(type, ActionPoints, AttackDmg, Health);
		});









		detailsBox.setStyle("-fx-border-color: black; -fx-border-width: 4px;"
				+ " -fx-padding: 10px; -fx-border-radius: 10px;");
		detailsBox.setPrefWidth(400);
		detailsBox.setPrefHeight(200);
		detailsBox.setAlignment(Pos.CENTER_LEFT);

		//Functionality:
		startButtonView.setOnMouseClicked(e -> {
			if(heroesList.getValue() != null)
				LoadMap(PrimaryStage, hm.get(heroesList.getValue()));
			else {
				Alert alert = new Alert(AlertType.ERROR);

				// Set the title and content text of the dialog
				alert.setTitle("Unvalid Action");
				alert.setHeaderText(null);
				alert.setContentText("Please Select A Hero");

				// Display the dialog box and wait for the user's response
				alert.showAndWait();

			}
		});


		//Layout Organization:
		topMenu.getChildren().addAll(selectHeroView);
		centerMenu.getChildren().addAll(heroesList, detailsBox);
		bottomMenu.getChildren().addAll(startButtonBox);


		VBox layout = new VBox();
		layout.setPadding(new Insets(20,20,20,20));
		layout.getChildren().addAll(topMenu,centerMenu, bottomMenu);

		StackPane s = new StackPane();
		s.getChildren().addAll(chaPic,layout);
		s.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundImage(image7, BackgroundRepeat.NO_REPEAT, null, null, new BackgroundSize(1600,1000, false, false, false, false))));


		Scene loadHero = new Scene(s, 1550, 790);

		PrimaryStage.setScene(loadHero);
		PrimaryStage.show();
	}

	public void LoadMap(Stage primaryStage, int h) {	
		//		DESIGN :
		//
		//			- HBox MainPlane -> Includes Both 1)GridPane Map & 2)VBox Details
		//
		//			- GridPane Map 
		//
		//			-VBox Details -> Includes 
		//			        1) HBox CurrHero -> Includes 
		//			                     1) VBox CurrAttr  & 2) VBox CurrDet ->
		//			                                         Includes 1)HBox Health -> Label "Health" & ProgressBar healthBar, 2) HBox SuppVacc
		//			         2) HBox SelectableHeroes -> Includes
		//			                    1) ComboBox SelectHero & 2) VBox SelectableAttr
		//			         3) HBox Abilities -> Includes
		//			                     1) ImageView Special & 2) ImageView EndTurn
		//			+++++++++
		//			CONTROL: 
		//			1) MouseActions W, A, S, D use selected hero ( h = ComboBox.getValue()) to call h.move(Direction.__) / Afterwards, call updateMapAndHeroBox
		//			2)Exception Arises -> Alert Box

		Hero startHero = Game.availableHeroes.get(h);
		Game.startGame(startHero);

		mapGrid = new GridPane();
		mapGrid.setPadding(new Insets(5));
		mapGrid.setHgap(10);
		mapGrid.setVgap(10);

		for (int row = 0; row < 15; row++) {
			for (int col = 0; col < 15; col++) {
				Rectangle cell = new Rectangle(40, 40); 

				if(Game.map[row][col].isVisible() == false) {
					cell.setFill(Color.GRAY);
				}

				if(Game.map[row][col].isVisible() == true) {
					cell.setFill(Color.WHITE);
					if(((CharacterCell)Game.map[row][col]).getCharacter() instanceof Zombie) {
						cell.setFill(Color.GREEN);
					}
				}
				if(Game.map[row][col] instanceof CharacterCell && ((CharacterCell)Game.map[row][col]).getCharacter() != null && ((CharacterCell)Game.map[row][col]).getCharacter().equals(startHero)) {
					cell.setFill(Color.RED);
				}

				mapGrid.add(cell, col, 14-row);
			}
		}


		HBox mainPlane = new HBox();
		mainPlane.setPadding(new Insets(10));
		Image image7 = new Image("views/the_last_of_us_7-wallpaper-3840x2160.jpg");




		Details = new VBox();
		Details.setPadding(new Insets(20));

		//First Row
		currHero = new HBox();
		currHero.setAlignment(Pos.TOP_RIGHT);
		currHero.setSpacing(30);

		Hero currentHero = startHero;


		currAttr = new VBox();
		currAttr.setTranslateX(-50);
		Label currName = new Label("Name :" + currentHero.getName() + " ");
		Label actionPoints = new Label("Action Points :" + currentHero.getActionsAvailable() + " ");
		Label attackDmg = new Label("Attack Damage :" + currentHero.getAttackDmg() + " ");
		Label type = new Label(getTypeOfHero(currentHero));

		currAttr.getChildren().addAll(currName, actionPoints, attackDmg, type);
		currName.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 18));
		actionPoints.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 18));
		attackDmg.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 18));
		type.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 18));

		String textColor = "-fx-text-fill: white;"; // Change the text color to blue

		actionPoints.setStyle(textColor);
		attackDmg.setStyle(textColor);
		type.setStyle(textColor);
		currName.setStyle(textColor);

		VBox currDetails = new VBox();
		HBox health = new HBox();

		healthBar = new Label("Current Health : " + startHero.getCurrentHp());
		healthBar.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 24));
		health.getChildren().addAll( healthBar);

		SuppVacc = new VBox();
		Label currentSupplies = new Label("Supplies Available : " + currentHero.getSupplyInventory().size());
		Label currentVaccines = new Label("Vaccines Available : " + currentHero.getVaccineInventory().size());
		currentSupplies.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 18));
		currentVaccines.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 18));

		healthBar.setStyle(textColor);
		currentSupplies.setStyle(textColor);
		currentVaccines.setStyle(textColor);

		SuppVacc.getChildren().addAll(currentSupplies, currentVaccines);

		currDetails.getChildren().addAll(healthBar, SuppVacc);

		currHero.getChildren().addAll(currAttr, currDetails);

		//Second Row
		HBox heroesToSelect = new HBox();
		heroesToSelect.setAlignment(Pos.CENTER_RIGHT);



		selectableHeroes = new ComboBox<String>();
		selectableHeroes.setPromptText("Select Another Hero!");
		selectableHeroes.setTranslateX(-10);
		selectableHeroes.setStyle("-fx-border-color: white; -fx-border-radius: 15px ;-fx-background-color: white;");

		for(Hero hero : Game.heroes) {
			selectableHeroes.getItems().add(hero.getName());
		}
		//	selectableHeroes.getItems()
		HBox selHeroes = new HBox(selectableHeroes);
		selHeroes.setAlignment(Pos.CENTER_LEFT);

		VBox selectableAttr = new VBox();

		selectableAttr.setStyle("-fx-border-color: black; -fx-border-width: 2px;"
				+ " -fx-padding: 10px; -fx-border-radius: 15px;");
		selectableAttr.setPrefWidth(400);
		selectableAttr.setPrefHeight(200);
		selectableAttr.setAlignment(Pos.CENTER_RIGHT);


		selectableHeroes.setOnAction(e -> {
			try {
				Hero selectedHero = Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex());
				if(selectableAttr.getChildren() != null) {
					selectableAttr.getChildren().clear();
				}
				Label selName = new Label("Name :" + selectedHero.getName() + " ");
				Label selActionPoints = new Label("Action Points :" + selectedHero.getActionsAvailable() + " ");
				Label selAttackDmg = new Label("Attack Damage :" + selectedHero.getAttackDmg() + " ");
				Label selType = new Label(getTypeOfHero(selectedHero));

				selName.setStyle(textColor);
				selActionPoints.setStyle(textColor);
				selAttackDmg.setStyle(textColor);
				selType.setStyle(textColor);


				selectableAttr.getChildren().addAll(selName, selActionPoints, selAttackDmg, selType);
				selName.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 20));
				selActionPoints.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 20));
				selAttackDmg.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 20));
				selType.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 20));
				UpdateSelectedChamp(selectedHero);
			}catch(IndexOutOfBoundsException e1) {
				alertSystem("Please choose a hero first!", "Choose a hero please");
			}


		});

		heroesToSelect.getChildren().addAll(selHeroes, selectableAttr);
		//Third Row
		HBox rowOne = new HBox();
		HBox rowTwo = new HBox();
		rowOne.setSpacing(15);
		rowTwo.setSpacing(15);
		rowOne.setPadding(new Insets(5));
		rowTwo.setPadding(new Insets(5));
		rowOne.setTranslateY(65);
		rowTwo.setTranslateY(-5);

		Image useSpecial = new Image("views/UseSpecial.png");
		ImageView useSpecialView = new ImageView(useSpecial);
		useSpecialView.setFitHeight(150);
		useSpecialView.setFitWidth(150);
		useSpecialView.setTranslateX(460);
		useSpecialView.setTranslateY(70);

		Image endTurn = new Image("views/EndTurn.png");
		ImageView endTurnView = new ImageView(endTurn);

		endTurnView.setTranslateX(-200);
		endTurnView.setTranslateY(75);

		Image attack = new Image("views/vecteezy_an-8-bit-retro-styled-pixel-art-illustration-of-a-stone-sword_19527033_273.png");
		ImageView attackView = new ImageView(attack);
		attackView.setFitHeight(180);
		attackView.setFitWidth(180);
		attackView.setTranslateX(450);
		attackView.setTranslateY(80);

		Image cure = new Image("views/pixil-frame-0 (1).png");
		ImageView cureView = new ImageView(cure);
		cureView.setFitHeight(300);
		cureView.setFitWidth(300);
		cureView.setTranslateX(-150);
		cureView.setTranslateY(80);


		rowOne.getChildren().addAll(attackView, cureView);
		rowTwo.getChildren().addAll(useSpecialView, endTurnView);
		VBox abilities = new VBox(rowOne, rowTwo);
		abilities.setTranslateY(-50);
		abilities.setAlignment(Pos.BOTTOM_RIGHT);
		abilities.setSpacing(10);

		if(Game.checkGameOver()) {
			GameOver();
		}

		useSpecialView.setOnMouseClicked(e -> {
			try {
				Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).useSpecial();
			} catch (NoAvailableResourcesException | InvalidTargetException e1) {
				if(e1 instanceof NoAvailableResourcesException) {
					alertSystem("No Available Resources", e1.getMessage());
				}else {
					alertSystem("Invalid Target", e1.getMessage());
				}
			}
			updateMap(currentHero);
		});


		cureView.setOnMouseClicked(e -> {
			try {
				Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).cure();
				if(Game.heroes.size() > 1) {
					selectableHeroes.getItems().clear();
					for(Hero hero : Game.heroes) {
						selectableHeroes.getItems().add(hero.getName());
					}
				}
			} catch (NoAvailableResourcesException | InvalidTargetException | NotEnoughActionsException e1) {
				if(e1 instanceof NoAvailableResourcesException) {
					alertSystem("No Available Resources", e1.getMessage());
				}else if(e1 instanceof InvalidTargetException) {
					alertSystem("Invalid Target", e1.getMessage());
				}else {
					alertSystem("Not Enough Actions", e1.getMessage());
				}
			}

		});

		endTurnView.setOnMouseClicked(e ->{
			try {
				Game.endTurn();
				updateMap(Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()));
				if(Game.heroes.size() > 1) {
					selectableHeroes.getItems().clear();
					for(Hero hero : Game.heroes) {
						selectableHeroes.getItems().add(hero.getName());
					}
				}
			} catch (NotEnoughActionsException | InvalidTargetException e1) {
				if(e1 instanceof InvalidTargetException) {
					alertSystem("Invalid Target", e1.getMessage());
				}else {
					alertSystem("Not ENough Actions", e1.getMessage());
				}
			}

		});

		attackView.setOnMouseClicked(e -> {
			try {
				Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).attack();
				updateMap(Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()));
			} catch (NotEnoughActionsException | InvalidTargetException e1) {
				if(e1 instanceof InvalidTargetException) {
					alertSystem("Invalid Target", e1.getMessage());
				}else {
					alertSystem("Not Enough Actions", e1.getMessage());
				}
			}
		});


		mapGrid.setAlignment(Pos.CENTER_LEFT);
		Details.setAlignment(Pos.CENTER_RIGHT);

		currHero.setStyle("-fx-border-color: black; -fx-border-width: 9px; -fx-border-radius: 3px; -fx-padding: 25px;");
		heroesToSelect.setStyle("-fx-border-color: black; -fx-border-width: 9px; -fx-border-radius: 3px; -fx-padding: 25px;");

		Details.getChildren().addAll(currHero, heroesToSelect, abilities);

		mainPlane.getChildren().addAll(mapGrid, Details);

		StackPane s = new StackPane();
		s.getChildren().add(mainPlane);
		s.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundImage(image7, BackgroundRepeat.NO_REPEAT, null, null, new BackgroundSize(1800,1000, false, false, false, false))));

		//Functionality: 
		selectableHeroes.setOnKeyPressed(e -> {
			KeyCode key = e.getCode();
			if(Game.heroes.size() == 0) {
				GameOver();
			}
			if(key == KeyCode.W) {
				try {
					Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).move(Direction.UP);
					if(Game.map[Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).getLocation().x][Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).getLocation().y] instanceof TrapCell) {
						alertSystem("Trap!", "You stepped onto a trap!");
					}
					updateMap(Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()));
				} catch (MovementException | NotEnoughActionsException | IndexOutOfBoundsException e1) {
					if(e1 instanceof MovementException) {
						alertSystem("Movement Exception",e1.getMessage());
					}else if(e1 instanceof NotEnoughActionsException){
						alertSystem("Not Enough Action Points",e1.getMessage());
					}else {
						alertSystem("Choose hero first!", "Please choose a hero first.");
					}
				}
			}
			if(key == KeyCode.A) {
				try {
					Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).move(Direction.LEFT);
					if(Game.map[Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).getLocation().x][Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).getLocation().y] instanceof TrapCell) {
						alertSystem("Trap!", "You stepped onto a trap!");
					}
					updateMap(Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()));
				} catch (MovementException | NotEnoughActionsException| IndexOutOfBoundsException e1) {
					if(e1 instanceof MovementException) {
						alertSystem("Movement Exception",e1.getMessage());
					}else if(e1 instanceof NotEnoughActionsException){
						alertSystem("Not Enough Action Points",e1.getMessage());
					}else {
						alertSystem("Choose hero first!", "Please choose a hero first.");
					}
				}
			}
			if(key == KeyCode.S) {
				try {
					Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).move(Direction.DOWN);
					if(Game.map[Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).getLocation().x][Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).getLocation().y] instanceof TrapCell) {
						alertSystem("Trap!", "You stepped onto a trap!");
					}
					updateMap(Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()));
				} catch (MovementException | NotEnoughActionsException| IndexOutOfBoundsException e1) {
					if(e1 instanceof MovementException) {
						alertSystem("Movement Exception",e1.getMessage());
					}else if(e1 instanceof NotEnoughActionsException){
						alertSystem("Not Enough Action Points",e1.getMessage());
					}else {
						alertSystem("Choose hero first!", "Please choose a hero first.");
					}
				}
			}
			if(key == KeyCode.D) {
				try {
					Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).move(Direction.RIGHT);
					if(Game.map[Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).getLocation().x][Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).getLocation().y] instanceof TrapCell) {
						alertSystem("Trap!", "You stepped onto a trap!");
					}
					updateMap(Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()));
				} catch (MovementException | NotEnoughActionsException| IndexOutOfBoundsException e1) {
					if(e1 instanceof MovementException) {
						alertSystem("Movement Exception",e1.getMessage());
					}else if(e1 instanceof NotEnoughActionsException){
						alertSystem("Not Enough Action Points",e1.getMessage());
					}else {
						alertSystem("Choose hero first!", "Please choose a hero first.");
					}
				}
			}
		});

		Scene scene = new Scene(s, 1550, 810);
		primaryStage.setFullScreen(true);
		primaryStage.setMaximized(true);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void UpdateSelectedChamp(Hero h) {
		((Label) currAttr.getChildren().get(0)).setText("Name :" + h.getName() + " ");
		((Label) currAttr.getChildren().get(1)).setText("Action Points :" + h.getActionsAvailable() + " ");
		((Label) currAttr.getChildren().get(2)).setText("Attack Damage :" + h.getAttackDmg() + " ");
		((Label) currAttr.getChildren().get(3)).setText(getTypeOfHero(h));
		healthBar.setText("Current Health : " + h.getCurrentHp());
		((Label)SuppVacc.getChildren().get(0)).setText("Supplies Available : " + h.getSupplyInventory().size());
		((Label)SuppVacc.getChildren().get(1)).setText("Vaccines Available : " + h.getVaccineInventory().size());

	}

	private void updateMap(Hero currentHero) {
		// Map Updates.

		for (int row = 0; row < 15; row++) {
			for (int col = 0; col < 15; col++) {
				Node cell = mapGrid.getChildren().get(row * 15 + col);
				if (cell instanceof Rectangle) {
					Rectangle rectangle = (Rectangle) cell;
					if(Game.map[row][col] instanceof TrapCell) {
						rectangle.setFill(Color.BROWN);
					}
					if(Game.map[row][col] instanceof CharacterCell && ((CharacterCell)Game.map[row][col]).getCharacter() != null && ((CharacterCell)Game.map[row][col]).getCharacter() instanceof Hero) {
						rectangle.setFill(Color.RED);

					}
					if(Game.map[row][col].isVisible() == false) {
						rectangle.setFill(Color.GRAY);
					}else if(Game.map[row][col].isVisible() == true && Game.map[row][col] instanceof CharacterCell && ((CharacterCell)Game.map[row][col]).getCharacter() instanceof Zombie){
						rectangle.setFill(Color.DARKGREEN);
					}else if(Game.map[row][col].isVisible() == true && Game.map[row][col] instanceof CollectibleCell &&((CollectibleCell)Game.map[row][col]).getCollectible() instanceof Vaccine){
						rectangle.setFill(Color.LIGHTGREEN);
					}else if(Game.map[row][col].isVisible() == true && Game.map[row][col] instanceof CollectibleCell &&((CollectibleCell)Game.map[row][col]).getCollectible() instanceof Supply){
						rectangle.setFill(Color.LIGHTBLUE);
					}else if(Game.map[row][col].isVisible() == true && Game.map[row][col] instanceof CharacterCell && ((CharacterCell)Game.map[row][col]).getCharacter() == null){
						rectangle.setFill(Color.WHITE);
					}

				}
			}
		}
		((Label) currAttr.getChildren().get(1)).setText("Action Points :" + currentHero.getActionsAvailable());



		healthBar.setText("Current Health : " + currentHero.getCurrentHp());

		if(Game.checkGameOver()) {
			GameOver();
		}

		if(Game.checkWin()) {
			youWon();
		}

		((Label)SuppVacc.getChildren().get(0)).setText("Supplies Available : " + currentHero.getSupplyInventory().size());
		((Label)SuppVacc.getChildren().get(1)).setText("Vaccines Available : " + currentHero.getVaccineInventory().size());


		//Setting zombie as a target
		HashMap<Integer, Integer> zombieLoc = getZombieLoc();
		HashMap<Integer,Integer> heroLoc=getHeroLoc();
		for(Entry<Integer, Integer> entry : zombieLoc.entrySet()) {
			int row = entry.getKey();
			int col = entry.getValue();

			mapGrid.getChildren().get(row*15 + col).setOnMouseClicked(e -> {
				try {
					Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).setTarget(((CharacterCell) Game.map[row][col]).getCharacter());
					if(e.getClickCount() > 1) {
						alertSystem("Target Selected", "You have already selected a target successfully.");
					}
				}catch(IndexOutOfBoundsException e1) {
					alertSystem("Please choose a hero!", "You need to choose a hero.");
				}
			});
		}
		for(Entry<Integer, Integer> entry : heroLoc.entrySet()) {
			int row = entry.getKey();
			int col = entry.getValue();

			mapGrid.getChildren().get(row*15 + col).setOnMouseClicked(e -> {
				try {
				Game.heroes.get(selectableHeroes.getSelectionModel().getSelectedIndex()).setTarget(((CharacterCell) Game.map[row][col]).getCharacter());
				if(e.getClickCount() > 1) {
					alertSystem("Target Selected", "You have already selected a target successfully.");
				}
				}catch(IndexOutOfBoundsException e1) {
					alertSystem("Please choose a hero!", "You need to choose a hero.");
				}
			});
		}

	}
	private void GameOver() {
		Stage EndStage = new Stage();
		HBox box = new HBox();


		Image startGameBackground = new Image("views/GameOver1.png");

		ImageView startGameBackgroundView = new ImageView(startGameBackground);
		startGameBackgroundView.setFitWidth(1000);
		startGameBackgroundView.setFitHeight(1200);

		startGameBackgroundView.fitWidthProperty().bind(EndStage.widthProperty());
		startGameBackgroundView.fitHeightProperty().bind(EndStage.heightProperty());
		box.getChildren().addAll(startGameBackgroundView);
		Scene GameOverScene = new Scene(box, 970, 700);		

		EndStage.setScene(GameOverScene);
		EndStage.showAndWait();
		Platform.exit();

	}
	private void youWon() {
		Stage VictoryStage = new Stage();
		HBox box = new HBox();
		Image victoryBackGround = new Image("views/win.png");
		ImageView victoryImageView = new ImageView(victoryBackGround);
		victoryImageView.fitWidthProperty().bind(VictoryStage.widthProperty());
		victoryImageView.fitHeightProperty().bind(VictoryStage.heightProperty());
		box.getChildren().addAll(victoryImageView);
		Scene GameOverScene = new Scene(box, 970, 700);		

		VictoryStage.setScene(GameOverScene);
		VictoryStage.showAndWait();
		Platform.exit();

	}

	public static void alertSystem(String title, String message) {
		Stage alertWindow = new Stage();

		alertWindow.initModality(Modality.APPLICATION_MODAL);
		alertWindow.setTitle(title);

		Label label = new Label();
		label.setText(message);
		label.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 20));
		Button closeButton = new Button("Close this window");
		closeButton.setOnAction(e -> alertWindow.close());

		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, closeButton);
		layout.setAlignment(Pos.CENTER);


		Scene scene = new Scene(layout, 500, 300);
		alertWindow.setScene(scene);
		alertWindow.showAndWait(); 
	}


	private String getTypeOfHero(Hero hero) {
		String s;
		if(hero instanceof Fighter) {
			s = "Type : Fighter";
		}else if(hero instanceof Medic) {
			s = "Type : Medic";
		}else{
			s = "Type : Explorer";
		}
		return s;
	}

	public static Hero heroGetter(String s) {
		Hero him =null;
		for(Hero h:Game.heroes) {
			if(h.getName().equals(s))
				him=h;
			break;
		}
		return him;
	}
	public static int heroGetter2(String s) {
		int i =-1;
		for(Hero h:Game.heroes) {
			if(h.getName().equals(s))
				return Game.heroes.indexOf(h);
		}
		return i;
	}

	public HashMap<Integer, Integer> getZombieLoc(){
		HashMap<Integer, Integer> locations = new HashMap<>();

		for(int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {
				Rectangle cell = (Rectangle) mapGrid.getChildren().get(i*15 + j);
				if(cell.getFill() == Color.DARKGREEN) {
					locations.put(i, j);
				}
			}
		}
		return locations;
	}
	public HashMap<Integer, Integer> getHeroLoc(){
		HashMap<Integer, Integer> locations = new HashMap<>();

		for(int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {
				Rectangle cell = (Rectangle) mapGrid.getChildren().get(i*15 + j);
				if(cell.getFill() == Color.RED) {
					locations.put(i, j);
				}
			}
		}
		return locations;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
