import type { BattleDTO } from "./battle";
import type { CharacterEnum } from "./characterEnum";

export type TurnDTO = {
  id: number;
  battleId: number;
  index: number;
  actor: CharacterEnum;
  currentHeroHp: number;
  currentEnemyHp: number;
  action: ActionDTO;
  creationTime: string;
  battle: BattleDTO
  
};

export const ActionTypes = {
  ATTACK: 'ATTACK',
  SKIP: 'SKIP',
  DEFEND: 'DEFEND',
  ESCAPE: 'ESCAPE',
} as const;

export type ActionType = typeof ActionTypes[keyof typeof ActionTypes];

export type ActionDTO = {
  type: ActionType;
  weight: number;
};


export const ActorTypes = {
  HERO: 'HERO',
  ENEMY: 'ENEMY'
} as const;

export type ActorType = typeof ActorTypes[keyof typeof ActorTypes];

