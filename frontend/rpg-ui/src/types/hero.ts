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
  status: Status;
  atk: Stat;
  def: Stat;
  lck: Stat;
}

export const Statuses = {
  DEAD: 'DEAD', 
  ALIVE: 'ALIVE'
} as const;

export type Status = typeof Statuses[keyof typeof Statuses];