require_relative 'Monster'
require_relative 'Player'
require_relative 'Orientation'
require_relative 'Directions'
require_relative 'Dice'

module Irrgarten
  class Labyrinth
    @@BLOCK_CHAR = 'X'
    @@EMPTY_CHAR = '-'
    @@MONSTER_CHAR = 'M'
    @@COMBAT_CHAR = 'C'
    @@EXIT_CHAR = 'E'
    @@ROW = 0
    @@COL = 1

    def initialize(n_rows, n_cols,exit_row,exit_col)
      @n_rows = n_rows
      @n_cols = n_cols
      @exit_row = exit_row
      @exit_col = exit_col
      @monsters = Array.new(n_rows){Array.new(n_cols)}
      @players = Array.new(n_rows){Array.new(n_cols)}
      @labyrinth = Array.new(n_rows){Array.new(n_cols,@@EMPTY_CHAR)}
      @labyrinth[exit_row][exit_col] = @@EXIT_CHAR
    end

    def spread_players(players)
      for i in 0...players.length
        p = players[i]
        pos = random_empty_pos
        put_player_2d(-1,-1,pos[@@ROW],pos[@@COL],p)
      end
    end

    def have_a_winner
      return @players[@exit_row][@exit_col] != nil
    end

    def to_s
      devolver = ""
      for i in 0...@n_rows
        devolver += "| "
        for j in 0...@n_cols
          devolver += @labyrinth[i][j].to_s + " "
        end
        devolver += " |\n"
      end

      return devolver

    end

    def add_monster(row,col,monster)
        if pos_ok(row,col) && @labyrinth[row][col] == @@EMPTY_CHAR
          @labyrinth[row][col] = @@MONSTER_CHAR
          @monsters[row][col] = monster
          monster.set_pos(row,col)
          add = false
        end

    end

    def put_player(direction,player)
      old_row = player.get_row
      old_col = player.get_col
      new_pos = dir2_pos(old_row,old_col,direction)
      monster = put_player_2d(old_row,old_col,new_pos[@@ROW],new_pos[@@COL],player)
      return monster
    end

    def add_block(orientation,start_row,start_col,length)
      if orientation == Orientation::VERTICAL
        inc_row = 1
        inc_col = 0
      else
        inc_row = 0
        inc_col = 1
      end
      row = start_row
      col = start_col

      while pos_ok(row,col) && empty_pos(row,col) && length > 0
        @labyrinth[row][col] = @@BLOCK_CHAR
        length -=1
        row += inc_row
        col += inc_col
      end
    end

    def valid_moves(row,col)
      output = Array.new
      if can_step_on(row+1,col)
        output.push(Directions::DOWN)
      end
      if can_step_on(row-1,col)
        output.push(Directions::UP)
      end
      if can_step_on(row,col+1)
        output.push(Directions::RIGHT)
      end
      if can_step_on(row,col-1)
        output.push(Directions::LEFT)
      end
      return output
    end

    def empty_pos(row,col)
      return @labyrinth[row][col] == @@EMPTY_CHAR
    end
    private

    def pos_ok(row,col)
      return row < @n_rows && col < @n_cols && row >= 0 && col >= 0
    end

    def monster_pos(row,col)
      return @labyrinth[row][col] == @@MONSTER_CHAR
    end

    def exit_pos(row,col)
      return @labyrinth[row][col] == @@EXIT_CHAR
    end

    def combat_pos(row,col)
      return @labyrinth[row][col] == @@COMBAT_CHAR
    end

    def can_step_on(row,col)
      return pos_ok(row,col) && (monster_pos(row,col) || empty_pos(row,col) || exit_pos(row,col))
    end

    def update_old_pos(row,col)
      if pos_ok(row,col)
        if combat_pos(row,col)
          @labyrinth[row][col] = @@MONSTER_CHAR
        else
          @labyrinth[row][col] = @@EMPTY_CHAR
        end
      end
    end

    def dir2_pos(row,col,direction)
      devolver = Array.new
      case direction
      when 'left'
        col -= 1
      when 'right'
        col += 1
      when 'up'
        row += 1
      else
        row -= 1
      end
      devolver.push(row,col)
      return devolver
    end

    def random_empty_pos
      devolver = Array.new
      begin
        num1 = Dice.random_pos(@n_rows)
        num2 = Dice.random_pos(@n_cols)
      end while empty_pos(num1,num2)
      devolver.push(num1,num2)
      return devolver
    end

    def put_player_2d(old_row,old_col,row,col,player)
      output = nil
      if can_step_on(row,col)
        if pos_ok(old_row,old_col)
          p = @players[old_row][old_col]
          if p == player
            update_old_pos(old_row,old_col)
            @players[old_row][old_col] = nil
          end
        end
        monster_pos = monster_pos(row,col)
        if monster_pos
          @labyrinth[row][col] = @@COMBAT_CHAR
          output = @monsters[row][col]
        else
          number = player.get_number
          @labyrinth[row][col] = number
        end
        @players[row][col] = player
        player.set_pos(row,col)
      end
      return output
    end
  end
end