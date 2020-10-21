import React from 'react';
import {
  cleanup,
  fireEvent,
  render,
  screen,
  wait,
} from '@testing-library/react';
import { MockedProvider } from '@apollo/client/testing';
import { MemoryRouter } from 'react-router-dom';
import StudentPageCont, {
  JOIN_GROUP_MUTATION,
  STUDENT_GROUPS_QUERY,
} from './StudentPageCont';
import { useAuthentication } from '../../AuthService';
import { cache as clientCache } from '../../ApolloClient';
import { waitNextTick } from '../../utils/test-utils';

jest.mock('../../AuthService');

const mockJoinGroupCodeInput = 'CODe56789ee';
const mockJoinGroupCodeValue = mockJoinGroupCodeInput
  .substr(0, 8)
  .toUpperCase();
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

const queryRequest = {
  request: {
    query: STUDENT_GROUPS_QUERY,
    variables: { userId: mockUser.id },
  },
  result: {
    data: {
      user: mockUser,
    },
  },
};

const queryError = {
  request: {
    query: STUDENT_GROUPS_QUERY,
    variables: { userId: mockUser.id },
  },
  error: new Error('Error :('),
};

describe('StudentPageComponent', () => {
  beforeEach(() => {
    useAuthentication.mockReturnValue({ userId: mockUser.id });
  });

  afterEach(() => {
    jest.clearAllMocks();
    cleanup();
  });

  test('shows data correctly from the api', async () => {
    render(
      <MockedProvider mocks={[queryRequest]}>
        <StudentPageCont />
      </MockedProvider>,
      { wrapper: MemoryRouter }
    );
    // await waitNextTick();
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

  test('handle join code changes correctly', async () => {
    render(
      <MockedProvider mocks={[queryRequest]} cache={clientCache}>
        <StudentPageCont />
      </MockedProvider>,
      { wrapper: MemoryRouter }
    );

    await screen.findByText(/student groups/i);

    expect(screen.getByRole('button')).toBeDisabled();

    fireEvent.change(screen.getByRole('textbox'), {
      target: { value: mockJoinGroupCodeInput },
    });

    expect(screen.getByRole('button')).toBeEnabled();
    expect(screen.getByRole('textbox')).toHaveValue(mockJoinGroupCodeValue);
  });

  test('render correctly when receives error from the api', async () => {
    render(
      <MockedProvider mocks={[queryError]}>
        <StudentPageCont />
      </MockedProvider>,
      { wrapper: MemoryRouter }
    );

    await waitNextTick();
    await screen.findByTestId('loading');
  });

  test('sends join code to the api on click', async () => {
    // let mutationCalled = false;
    const mutationRequest = {
      request: {
        query: JOIN_GROUP_MUTATION,
        variables: { userId: mockUser.id, groupCode: mockJoinGroupCodeValue },
      },
      // result: () => {
      //     mutationCalled = true;
      //     return {data: {addStudentToGroupFromCode: mockGroup1}}
      // },
      newData: jest.fn(),
    };

    render(
      <MockedProvider mocks={[queryRequest, mutationRequest]}>
        <StudentPageCont />
      </MockedProvider>,
      { wrapper: MemoryRouter }
    );

    await screen.findByText(/student groups/i);

    fireEvent.change(screen.getByRole('textbox'), {
      target: { value: mockJoinGroupCodeValue },
    });
    fireEvent.click(screen.getByRole('button'));

    // userEvent.click(screen.getByRole('button'), {button: 0})
    // waitNextTick();
    // screen.debug()

    wait(() => expect(mutationRequest.newData).toHaveBeenCalledTimes(1));

    // expect(mutationCalled).toBe(false);
  });
});
