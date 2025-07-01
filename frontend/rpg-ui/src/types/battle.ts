import type { CharacterSnapshot } from './characterSnapshot';
import type { TurnDTO } from './turn';

export const BattleStatuses = {
  ONGOING: 'ONGOING', 
  ENEMY_WIN: 'ENEMY_WIN',
  HERO_WIN: 'HERO_WIN',
  TO_START: 'TO_START',
  ESCAPED: 'ESCAPED'
} as const;

export type BattleStatus = typeof BattleStatuses[keyof typeof BattleStatuses];

export type BattleDTO = {
  id: number;
  heroId: number;
  heroSnapshot: CharacterSnapshot;
  enemySnapshot: CharacterSnapshot;
  status: BattleStatus;
  creationTime: string;
  lastUpdateTime: string;
  active: boolean;
  version: number;
  turns: TurnDTO[];
  startingPlayer: string
};


export type RewardDTO = {
  hero: CharacterSnapshot;
  expPack: ExpPackDTO;
};

export type ExpPackDTO = {
    lvExp: number;
    lckExp: number;
    atkExp: number;
    defExp: number;
    taken: boolean;
};