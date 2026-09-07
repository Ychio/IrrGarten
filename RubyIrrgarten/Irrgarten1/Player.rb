require_relative 'Dice'
require_relative 'Weapon'
require_relative 'Shield'
require_relative 'Directions'

module Irrgarten
  class Player
    @@MAX_WEAPONS = 2
    @@MAX_SHIELDS = 3
    @@INITIAL_HEALTH = 10
    @@HITS2LOSE = 3

    def initialize(number,intelligence,strength)
      @weapons = Array.new
      @shields = Array.new
      @name = "Player##{number}"
      @number = number
      @intelligence = intelligence
      @strength = strength
      @health = @@INITIAL_HEALTH
      @row = 0
      @col = 0
      @consecutive_hits = 0
    end

    def resurrect
      @weapons.clear
      @shields.clear
      @health = @@INITIAL_HEALTH
      @consecutive_hits = 0
    end

    def get_row
      return @row
    end

    def get_col
      return @col
    end

    def get_number
      return @number
    end

    def set_pos(row,col)
      @row = row
      @col = col
    end

    def dead
      return @health < 1
    end

    def move(direction,valid_moves)
      size = valid_moves.size
      contained = valid_moves.include?(direction)
      if size > 0 && !contained
        return valid_moves[0]
      else
        return direction
      end
    end

    def attack
      return @strength + sum_weapons
    end

    def defend(received_attack)
      manage_hit(received_attack)
    end

    def receive_reward
      w_reward = Dice.weapons_reward
      s_reward = Dice.shields_reward
      for i in 1..w_reward
        wnew = new_weapon
        receive_weapon(wnew)
      end

      for i in 1..s_reward
        snew = new_shield
        receive_shield(snew)
      end

      extrahealth = Dice.health_reward
      @health += extrahealth
    end

    def to_s
      devolver =  "Nombre: #{@name}  Intelligence: #{@intelligence}  Strength: #{@strength} Health: #{@health} Position: [#{@row},#{@col}] consecutive hits: #{@consecutive_hits}  \n"
      devolver+= "Weapons: \n"
      if @weapons.length > 0
        for i in 0...@weapons.length
          devolver += @weapons[i]
        end
        devolver += "Shields: \n"
        for i in 0...@shields.length
          devolver += @shields[i]
        end
      end
      return devolver
    end

    private

    def receive_weapon(w)

      for i in 0...@weapons.length
        wi = @weapons[i]
        discard = wi.discard
        if discard
          @weapons.delete(wi)
        end
      end
      size = @weapons.length
      if size < @@MAX_WEAPONS
        @weapons.push(wi)
      end
    end

    def receive_shield(s)
      for i in 0...@shields.length
        si = @shields[i]
        discard = s.discard
        if discard
          @shields.delete(si)
        end
      end
      size = @shields.length
      if size < @@MAX_SHIELDS
        @shields.push(si)
      end
    end

    def new_weapon
      w= Weapon.new(Dice.weapons_power,Dice.uses_left)
      return w
    end

    def new_shield
      s= Shield.new(Dice.weapons_power,Dice.uses_left)
      return s
    end

    def sum_weapons
      total = 0.0
      for i in 0...@weapons.length
        total += @weapons[i]
      end
      return total
    end

    def sum_shields
      total = 0.0
      for i in 0...@shields.length
        total += @shields[i]
      end
      return total
    end

    def defensive_energy
      return @intelligence + sum_shields
    end

    def manage_hit(received_attack)
      defense = defensive_energy
      if defense < received_attack
        got_wounded
        inc_consecutive_hits
      else
        reset_hits
      end

      if (@consecutive_hits == @@HITS2LOSE) || dead
        lose = true
        reset_hits
      else
        lose = false
      end
      return lose
    end

    def reset_hits
      @consecutive_hits = 0
    end

    def got_wounded
      @health-=1
    end

    def inc_consecutive_hits
      @consecutive_hits += 1
    end

  end
end