export interface Stat {
  level: number;
  exp: number;
}

export interface HeroDTO {
  id: number;
  name: string;
  maxHp: number;
  gold: number;
  level: number;
  exp: number;
  status: string;
  atk: Stat;
  def: Stat;
  lck: Stat;
}
