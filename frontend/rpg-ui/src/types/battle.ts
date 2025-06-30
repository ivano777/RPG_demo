import type { CharacterSnapshot } from './characterSnapshot';
import type { TurnDTO } from './turn';

export const BattleStatuses = {
  ONGOING: 'ONGOING', 
  ENEMY_WIN: 'ENEMY_WIN',
  HERO_WIN: 'HERO_WIN',
  TO_START: 'TO_START'
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
