require_relative 'UI/textUI'
require_relative  'Controller/controller'
require_relative 'Game'


module Irrgarten
  game = Game.new(3)
  game.next_step(Directions::UP)
  vista = UI::TextUI.new
  vista.show_game(game.get_game_state)
  controller = Control::Controller.new(game,vista)
  controller.play
end