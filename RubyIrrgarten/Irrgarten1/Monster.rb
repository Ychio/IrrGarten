require_relative 'Dice'

module Irrgarten
  class Monster
    @@INITIAL_HEALTH = 5
    @@OUT_LABYRINTH = -1

    def initialize(name,intelligence,strength)
      @name = name
      @intelligence = intelligence
      @strength = strength
      @health = @@INITIAL_HEALTH
      @row = @@OUT_LABYRINTH
      @col = @@OUT_LABYRINTH
    end

    def dead
      return @health < 1
    end

    def attack
      return Dice.intensity(@strength)
    end

    def defend(received_attack)
      isDead = dead
      if !isDead
        defensive_energy = Dice.intensity(@intelligence)
        if defensive_energy < received_attack
          got_wounded
          isDead = dead
        end
      end
      return isDead
    end

    def set_pos(row,col)
      @row = row
      @col = col
    end

    def to_s
      return "Name: #{@name} intelligence: #{@intelligence} strength: #{@strength} health: #{@health} position: #{@row},#{@col}"
    end

    private
    def got_wounded
      @health -= 1
    end
  end
end