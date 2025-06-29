interface Props {
  current: number;
  max: number;
  color?: string;
}

export default function HealthBar({ current, max, color = 'green' }: Props) {
  const percentage = Math.max(0, Math.min(100, (current / max) * 100));

  return (
    <div style={{
      backgroundColor: '#444',
      borderRadius: '4px',
      width: '100%',
      height: '0.75rem',
      overflow: 'hidden',
      marginTop: '4px'
    }}>
      <div style={{
        height: '100%',
        width: `${percentage}%`,
        backgroundColor: color,
        transition: 'width 0.3s ease'
      }} />
    </div>
  );
}
