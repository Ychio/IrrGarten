require_relative 'Dice'
module Irrgarten
  class Weapon
   def initialize(power,uses)
      @power = power
      @uses = uses
    end

   def attack
     if @uses > 0
       @uses -= 1
        @power.to_s
       else
       @uses.to_s
     end
    end

    def to_s
      return "W" + @power.to_s + "," + @uses.to_s + "]" + "/n"
    end
   def discard
     Dice.discard_element(@uses)
   end
  end
end