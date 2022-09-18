import React from 'react';
import { cleanup, fireEvent, render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import StudentPageComp from './StudentPageComp';

const mockJoinGroupCodeInput = 'CODe56789ee';
const mockGroup1 = {
  id: 2,
  name: 'Group1',
  news: 'News1',
  newsChangedDate: new Date(),
};
const mockGroup2 = {
  id: 3,
  name: 'Group2',
  news: 'News2',
  newsChangedDate: new Date(),
};
const mockUser = {
  id: 1,
  name: 'User Name',
  code: 'CODE0001',
  studentGroups: [mockGroup1, mockGroup2],
};

describe('StudentPageComponent', () => {
  let props = null;

  beforeEach(() => {
    props = {
      user: mockUser,
      joinCode: mockJoinGroupCodeInput,
      joinDisabled: true,
      onJoinCodeChange: jest.fn(),
      onJoinGroup: jest.fn(),
    };
  });

  afterEach(() => {
    jest.clearAllMocks();
    cleanup();
  });

  test('shows received data correctly', async () => {
    render(<StudentPageComp {...props} />, { wrapper: MemoryRouter });

    await screen.findByText(/student groups/i);

    expect(screen.getByText(mockUser.name)).toBeInTheDocument();
    expect(screen.getByText(mockUser.code)).toBeInTheDocument();

    expect(screen.getAllByRole('listitem').length).toBe(2);
    expect(screen.getAllByRole('listitem')[0]).toHaveTextContent(
      mockGroup1.name
    );
    expect(screen.getAllByRole('listitem')[1]).toHaveTextContent(
      mockGroup2.name
    );

    expect(screen.getAllByRole('link').length).toBe(2);
    expect(screen.getAllByRole('link')[0]).toHaveTextContent(mockGroup1.name);
    expect(screen.getAllByRole('link')[1]).toHaveTextContent(mockGroup2.name);
  });

  test('handle join code changes', async () => {
    render(<StudentPageComp {...props} />, { wrapper: MemoryRouter });

    await screen.findByText(/student groups/i);

    fireEvent.change(screen.getByRole('textbox'), {
      target: { value: '' },
    });

    expect(props.onJoinCodeChange).toHaveBeenCalledTimes(1);
    expect(props.onJoinCodeChange).toHaveBeenCalledWith('');
  });

  test('sends join code on click when it is enabled', async () => {
    props.joinDisabled = false;

    render(<StudentPageComp {...props} />, { wrapper: MemoryRouter });

    await screen.findByText(/student groups/i);

    fireEvent.click(screen.getByRole('button'));

    expect(props.onJoinGroup).toHaveBeenCalledTimes(1);
  });

  test('does not send join code on click when it is disabled', async () => {
    render(<StudentPageComp {...props} />, { wrapper: MemoryRouter });

    await screen.findByText(/student groups/i);

    fireEvent.click(screen.getByRole('button'));

    expect(props.onJoinGroup).toHaveBeenCalledTimes(0);
  });
});
