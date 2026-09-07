module Irrgarten
  class Dice
    @@MAX_USES = 5
    @@MAX_INTELLIGENCE = 10.0
    @@MAX_STRENGTH = 10.0
    @@RESURRECT_PROB = 0.3
    @@WEAPONS_REWARDS = 2
    @@SHIELDS_REWARD = 3
    @@HEALTH_REWARD = 5
    @@MAX_ATTACK = 3
    @@MAX_SHIELD = 2
    @@generator = Random.new

    def self.random_pos(max)
      @@generator.rand(max)
    end

    def self.who_starts(nplayers)
      @@generator.rand(nplayers)
    end

    def self.random_intelligence
      @@generator.rand * @@MAX_INTELLIGENCE
    end

    def self.random_strength
      @@generator.rand * @@MAX_STRENGTH
    end

    def self.resurrect_player
      if @@generator.rand < 0.3
        true
      else
        false
      end
    end

    def self.weapons_reward
      @@generator.rand(@@WEAPONS_REWARDS)
    end

    def self.shields_reward
      @@generator.rand(@@SHIELDS_REWARD)
    end

    def self.health_reward
      @@generator.rand(@@HEALTH_REWARD+1)
    end

    def self.weapons_power
      @@generator.rand(@@MAX_ATTACK+1)
    end

    def self.shield_power
      @@generator.rand(@@MAX_SHIELD+1)
    end

    def self.uses_left
      @@generator.rand(@@MAX_USES+1)
    end

    def self.intensity(competence)
      @@generator.rand(0.0..competence.to_f)
    end

    def self.discard_element(uses_left)
      if @@generator.rand > uses_left.to_f/@@MAX_USES
        true
      else
        false
      end
    end
  end
end