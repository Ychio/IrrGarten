require_relative 'GameCharacter'
require_relative 'GameState'
require_relative 'Directions'
require_relative 'Player'
require_relative 'Dice'
require_relative 'Monster'
require_relative 'Labyrinth'
require_relative 'Orientation'

module Irrgarten
  class Game
    @@MAX_ROUNDS = 10
    @@n_rows = 7
    @@n_cols = 7
    @@exit_row = 2
    @@exit_col = 2
    @@n_monsters = 3
    def initialize(nplayers)
      @labyrinth = Labyrinth.new(@@n_rows, @@n_cols, @@exit_row, @@exit_col)
      @monsters = Array.new
      @players = Array.new
      for i in 0...nplayers
        p = Player.new(i,Dice.random_intelligence,Dice.random_strength)
        @players.push(p)
      end
      @current_player_index = Dice.who_starts(nplayers)
      @current_player = @players[@current_player_index]
      @log = ""
      @labyrinth.spread_players(@players)
      configure_labyrinth
    end

    def finished
      @labyrinth.have_a_winner
    end

    def next_step(preferred_direction)
      log = ""
      dead = @current_player.dead
      if !dead
        direction = actual_direction(preferred_direction)
        if direction != preferred_direction
          log_player_no_orders
        end
        monster = @labyrinth.put_player(direction,@current_player)
        if monster == nil
          log_no_monster
        else
          winner = combat(monster)
          manage_reward(winner)
        end
      else
        manage_resurrection
      end
      end_game = finished
      if !end_game
        next_player
      end
      return end_game
    end

    def get_game_state
      players = ""
      monsters = ""
      for i in 0...@players.length
        players += "#{@players[i].to_s} \n"
      end
      for i in 0...@monsters.length
        monsters += "#{@monsters[i].to_s} \n"
      end
      gamestate = GameState.new(@labyrinth.to_s,players,monsters,@current_player_index,self.finished,@log)
      return gamestate
    end

    private
    def configure_labyrinth
      @labyrinth.add_block(Orientation::HORIZONTAL,2,0,2)
      for i in 0...@@n_monsters
        name = "Monster#{(i+1).to_s}"
        intelligence = Dice.random_intelligence
        strength = Dice.random_strength
        @monsters[i] = Monster.new(name,intelligence,strength)

        begin
          n1 = Dice.random_pos(@@n_rows)
          n2 = Dice.random_pos(@@n_cols)
        end while !@labyrinth.empty_pos(n1,n2)
        @labyrinth.add_monster(n1,n2,@monsters[i])
      end
    end

    def next_player
      @current_player_index += 1
      if @current_player_index >= @players.length
        @current_player_index = 0
      end
      @current_player = @players[@current_player_index]
    end

    def actual_direction(preferred_direction)
      current_row = @current_player.get_row
      current_col = @current_player.get_col
      valid_moves = @labyrinth.valid_moves(current_row, current_col)
      output = @current_player.move(preferred_direction, valid_moves)
      return output
    end

    def combat(monster)
      rounds = 0
      winner = GameCharacter::PLAYER
      player_attack = @current_player.attack
      lose = monster.defend(player_attack)
      while !lose && rounds < @@MAX_ROUNDS
        winner = GameCharacter::MONSTER
        rounds+=1
        monster_attack = monster.attack
        lose = @current_player.defend(monster_attack)
        if !lose
          player_attack = @current_player.attack
          winner = GameCharacter::PLAYER
          monster.defend(player_attack)
        end
      end
      log_rounds(rounds,@@MAX_ROUNDS)
      return winner
    end

    def manage_reward(winner)
      if winner == GameCharacter::PLAYER
        @current_player.receive_reward
        log_player_won
      else
        log_monster_won
      end
    end

    def manage_resurrection
      resurrect = Dice.resurrect_player
      if resurrect
        @current_player.resurrect
        log_resurrected
      else
        log_player_skip_turn
      end
    end
    def log_player_won
      @log += "Player won \n"
    end

    def log_monster_won
      @log += "Monster won \n"
    end

    def log_resurrected
      @log += "Resurrected \n"
    end

    def log_player_skip_turn
      @log += "Jugador perdio el turnno por estar muerto\n"
    end

    def log_player_no_orders
      @log += "no fue posible las instrucciones del jugador \n"
    end

    def log_no_monster
      @log += "Movido a una celda vacia/no le ha sido moverse\n"
    end

    def log_rounds(rounds,max)
      @log += "#{rounds} / #{max}\n"
    end
  end
end