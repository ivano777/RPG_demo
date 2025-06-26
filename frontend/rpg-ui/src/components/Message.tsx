interface MessageProps {
  text: string;
  type: 'error' | 'success';
}

export default function Message({ text, type }: MessageProps) {
  const color = type === 'error' ? 'red' : 'lightgreen'

  return (
    <p style={{ color, marginTop: '0.5rem' }}>
      {text}
    </p>
  )
}
