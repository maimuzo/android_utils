class CreateUserbasics < ActiveRecord::Migration
  def self.up
    create_table :userbasics do |t|
      t.string :key
      t.string :name

      t.timestamps
    end
  end

  def self.down
    drop_table :userbasics
  end
end
