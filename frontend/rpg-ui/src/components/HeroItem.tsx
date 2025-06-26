import { Hero } from '../types/hero';

export default function HeroItem({ hero }: { hero: Hero }) {
  return (
    <article className="bg-white shadow rounded p-4 flex justify-between">
      <span className="font-medium">{hero.name}</span>
    </article>
  );
}
